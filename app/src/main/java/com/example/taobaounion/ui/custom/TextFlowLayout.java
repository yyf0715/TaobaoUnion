package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {


    public static final float DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowTextItemClickListener mItemClickListener = null;

    public int getContentSize() {
        return mTextList.size();//每一行长度
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;//水平间距
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;//垂直间距
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    private List<String> mTextList = new ArrayList<>();//存储的text值

    public TextFlowLayout(Context context) {
        this(context,null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        //去拿到相关属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);//获取属性值
        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace,DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace,DEFAULT_SPACE);
        //获取属性值，默认为DEFAULT_SPACE 10 px
        ta.recycle();//回收资源

        LogUtils.d(this,"mItemHorizontalSpace == > " + mItemHorizontalSpace);
        LogUtils.d(this,"mItemVerticalSpace == > " + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        removeAllViews();//避免重复出现子组件
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);//方法被用来反转指定列表中的元素的顺序  按照最近搜索排序
        //遍历内容
        for(String text : mTextList) {
            //添加子view
            //LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view,this,true);
            // 等价于
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view,this,false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null) {
                        mItemClickListener.onFlowItemClick(text);
                    }
                }
            });
            addView(item);//添加组件
        }
    }

    //这个是描述所有的行 lines.size(); 有行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {//测量每一行放多少个
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(getChildCount() == 0) {//没有记录
            return;
        }
        //这个是描述单行
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        LogUtils.d(this,"mSelfWidth == > " + mSelfWidth);
        //测量
        LogUtils.d(this,"onMeasure -- > " + getChildCount());
        //测量孩子
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if(itemView.getVisibility() != VISIBLE) {
                //如果该组件没有可视化，则不需要进行测量
                continue;
            }
            //测量前
            //LogUtils.d(this,"before height -- > " + itemView.getMeasuredHeight());
            measureChild(itemView,widthMeasureSpec,heightMeasureSpec);
            //为指定的子视图进行measure()操作  measure()确认子控件的大小
            //测量后
            // LogUtils.d(this,"after height -- > " + itemView.getMeasuredHeight());
            if(line == null) {
                //说明当前行为空，可以添加进来
                line = createNewLine(itemView);
            } else {
                //判断是否可以再继续添加
                if(canBeAdd(itemView,line)) {
                    //可以添加，添加去
                    line.add(itemView);
                } else {
                    //新创建一行
                    line = createNewLine(itemView);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        //测量自己
        setMeasuredDimension(mSelfWidth,selfHeight);//避免出现异常导致程序直接崩溃
       /* <p>此方法必须由{@link #onMeasure（int，int）}调用以存储
        测量的宽度和测量的高度。 否则将触发
        测量时出现异常。</ p>*/
    }

    private List<View> createNewLine(View itemView) {//创建空的一行
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以再继续添加新数据
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView,List<View> line) {
        //所有已经添加的子view宽度相加+(line.size()+1)*mItemHorizontalSpace + itemView.getMeasuredWidth()
        //条件：如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        int totalWith = itemView.getMeasuredWidth();//获取子组件的宽度
        for(View view : line) {
            //叠加所有已经添加控件的宽度
            totalWith += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWith += mItemHorizontalSpace * (line.size() + 1);//子组件的数量乘以间距加一，保证前后都有一定的距离，防止出现子组件太过于靠边
        //LogUtils.d(this,"totalWith -- > " + totalWith);
        //LogUtils.d(this,"mSelfWidth -- > " + mSelfWidth);
        //如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        return totalWith <= mSelfWidth;
    }

    //  布局
    @Override
    protected void onLayout(boolean changed,int l,int t,int r,int b) {
        //摆放孩子
        LogUtils.d(this,"onLayout -- > " + getChildCount());
        int topOffset = (int) mItemHorizontalSpace;
        for(List<View> views : lines) {//lines已经分好 行 了
            //views是每一行
            int leftOffset = (int) mItemHorizontalSpace;
            for(View view : views) {
                //每一行里的每个item   建议画图容易理解
                view.layout(leftOffset,topOffset,leftOffset + view.getMeasuredWidth(),topOffset + view.getMeasuredHeight());
                //
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;//每个子组件的宽度加上间距
            }
            topOffset += mItemHeight + mItemHorizontalSpace;//每一行的高度加上间距
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface OnFlowTextItemClickListener {
        void onFlowItemClick(String text);
    }
}
