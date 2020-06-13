package com.example.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketPageCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPageCallback {

    private ITicketPresenter mTicketPresenter;

    private boolean mHasTaobaoApp = false;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_back_press)
    public View backPress;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mOpenOrCopyBtn;

    @BindView(R.id.ticket_cover_loading)
    public View loadingView;

    @BindView(R.id.ticket_cover_retry)
    public TextView retryLoadText;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }

        //判断是否安装有淘宝
        // act=android.intent.action.MAIN
        // cat=[android.intent.category.LAUNCHER]
        // flg=0x10200000
        // cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
        //包名是这个：com.taobao.taobao
        //检查是否有安装淘宝应用
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobaoApp = packageInfo != null;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobaoApp = false;
        }
        //LogUtils.d(this,"mHasTaobaoApp-->>"+mHasTaobaoApp);

        //根据这个值来修改UI
        mOpenOrCopyBtn.setText(mHasTaobaoApp ? "打开淘宝领券" : "复制淘口令");
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    //生成淘宝指令Activity
    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制淘口令
                //拿到内容
                String ticketCode = mTicketCode.getText().toString().trim();
                LogUtils.d(TicketActivity.this, "淘口令为--->" + ticketCode);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);//CLIPBOARD_SERVICE访问和修改
                //复制到粘贴板上
                ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);//纯文本粘贴内容
                cm.setPrimaryClip(clipData);

                //判断有没有淘宝

                if (mHasTaobaoApp) {
                    //如果有就打开淘宝
                    Intent taobaoIntent = new Intent();
//                    taobaoIntent.setAction("android,intent.action.MAIN");
//                    taobaoIntent.addCategory("android,intent.category.LAUNCHER");
                    ComponentName componentName = new ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                } else {
                    //如果没有就提示复制成功
                    ToastUtil.showToast("已经复制,粘贴分享，或打开淘宝");
                }


            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }


    //实现ITicketPageCallback接口
    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (mCover != null && cover != null) {
            //LogUtils.d(this,"mCoverWidth--->"+targetWith);
            String coverPath = UrlUtils.getCoverPath(cover);
            //LogUtils.d(this,"coverPath:"+coverPath);
            Glide.with(this).load(coverPath).into(mCover);
        }

        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            mTicketCode.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {

        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }

        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
