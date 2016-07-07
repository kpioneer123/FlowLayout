package com.cloudhome.flowlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xionghu on 2016/7/7.
 * Email：965705418@qq.com
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        this(context,null);

    }

    public FlowLayout(Context context, AttributeSet attrs) {

        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

         //测量模式 EXACTLY
        int sizeWidth  = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft() -getPaddingRight();
        int modeWidth  = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec)  - getPaddingBottom() - getPaddingTop();
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content
        int width  = 0;
        int height = 0;

        //记录每一行的高度和宽度
        int lineWidth  = 0;
        int lineHeight = 0;

        //记录内部元素的个数
        int cCount = getChildCount();
        for(int i =0;i<cCount;i++ )
        {
            View child = getChildAt(i);

            measureChild(child ,widthMeasureSpec,heightMeasureSpec);
            //得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();

            //子View 占据的宽度
            int childWidth  = child.getMeasuredWidth()  + lp.leftMargin + lp.rightMargin;

            //子view 占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin  + lp.bottomMargin;

            if(lineWidth + childWidth >sizeWidth)
            {

                //对比得到最大的宽度
                width     = Math.max(lineWidth,childWidth);// 取最大的

                //重置lineWidth
                lineWidth = childWidth;// 重新开启新行，开始记录

                // 叠加当前高度
                height +=lineHeight;
                // 开启记录下一行的高度
                lineHeight = childHeight;


            }else{//为换行

                //叠加行宽
                lineWidth +=childWidth;

                lineHeight=Math.max(lineHeight,childHeight);
            }

            //最后一个控件
            if(i==cCount-1)
            {
                width  =Math.max(lineWidth,width);
                height +=lineHeight;
            }
        }


        Log.d("TAG","sizeWidth = "  + sizeWidth);
        Log.d("TAG","sizeHeight = " + sizeHeight);
            setMeasuredDimension(
                    //
                    modeWidth==MeasureSpec.EXACTLY?sizeWidth:width    + getPaddingLeft()  + getPaddingRight(),
                    modeHeight==MeasureSpec.EXACTLY?sizeHeight:height + getPaddingTop() + getPaddingBottom()
            );


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 存储所有的View
     */
    private  List<List<View>> mAllviews    = new ArrayList<List<View>>();

    /**
     * 每一行的高度
     */
    private  List<Integer>    mLineHeight  = new ArrayList<Integer>();



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        mAllviews.clear();
        mLineHeight.clear();

        //当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth  = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int cCount = getChildCount();

        for(int i = 0 ;i < cCount;i++)
        {
            View child = getChildAt(i);
            MarginLayoutParams lp =(MarginLayoutParams) child.getLayoutParams();

            int childWidth  = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if(childWidth + lineWidth + lp.leftMargin +lp.rightMargin >width -getPaddingLeft() -getPaddingRight())
            {
                //记录LineHeight
                mLineHeight.add(lineHeight);

                //记录当前行的Views
                mAllviews.add(lineViews);

                //重置我们的行宽和行高
                lineWidth  = 0;


                //重置我们的View集合
                lineViews =new ArrayList<View>();


            }

                lineWidth +=childWidth +lp.leftMargin + lp.rightMargin;
                lineHeight = Math.max(lineHeight,childHeight + lp.topMargin + lp.bottomMargin);
                lineViews.add(child);



        }


        // for end
        //处理最后一行

        mLineHeight.add(lineHeight);
        mAllviews.add(lineViews);

        //设置子View的位置

        int left = getPaddingLeft() ;
        int top  = getPaddingTop() ;

        //记录最后一行
        int lineNums= mAllviews.size();

        for(int i = 0 ;i < lineNums ; i++)
        {
            //当前行的所有的view
            lineViews  = mAllviews.get(i);

            //当前最大的高度
            lineHeight = mLineHeight.get(i);

            Log.e("TAG", "第" + i + "行 ：" + lineViews.size() + " , " + lineViews);
            Log.e("TAG", "第" + i + "行， ：" + lineHeight);
            for(int j= 0; j < lineViews.size();j++)
            {
                View child = lineViews.get(j);

                //判断child的状态
                if(child.getVisibility() == View.GONE)
                {
                    continue;
                }

               MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top  + lp.topMargin;
                int rc = lc   + child.getMeasuredWidth();
                int bc = tc   + child.getMeasuredHeight();

                //为子View进行布局
                child.layout(lc,tc,rc,bc);

                left += child.getMeasuredWidth() + lp.leftMargin +lp.rightMargin;

            }

            left = getPaddingLeft();
            top += lineHeight;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams——————MarginLayoutParams，child.getLayoutParams()为 MarginLayoutParams
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {

        return new MarginLayoutParams(getContext(),attrs);

    }
}
