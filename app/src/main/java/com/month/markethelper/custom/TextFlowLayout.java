package com.month.markethelper.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.month.markethelper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    private List<String> mTextList = new ArrayList<>();

    public static final int DEFAULT_SPACE = 10;

    private int mItemHorizontalSpace = DEFAULT_SPACE;

    private int mItemVerticalSpace = DEFAULT_SPACE;

    //组件的宽度
    private int mSelfWidth;

    //子组件的高度
    private int mItemHeight;

    //Item点击事件监听器
    private OnFlowTextItemClickListener mItemClickListener;

    public TextFlowLayout(Context context) {
        super(context);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取相关属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mItemHorizontalSpace = typedArray.getInteger(R.styleable.TextFlowLayout_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = typedArray.getInteger(R.styleable.TextFlowLayout_verticalSpace, DEFAULT_SPACE);
        //回收
        typedArray.recycle();
    }

    public void setItemHorizontalSpace(int itemHorizontalSpace) {
        this.mItemHorizontalSpace = itemHorizontalSpace;
    }

    public void setItemVerticalSpace(int itemVerticalSpace) {
        this.mItemVerticalSpace = itemVerticalSpace;
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        this.mTextList = textList;
        //反转内容，确保最新的历史记录在最前面
//        Collections.reverse(mTextList);
        //遍历内容
        for (int i = 0; i < mTextList.size(); i++) {
            //添加子View
            String text = mTextList.get(i);
            final int index = i;
            LinearLayout item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_goods_category, this, false);
            TextView textView = item.findViewById(R.id.category_tv);
            textView.setText(text);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onFlowTextItemClick(index, text);
                    }
                }
            });
            textView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onFlowTextItemLongChick(index, text);
                    }
                    return true;
                }
            });
            this.addView(item);
        }
    }

    //保存所有行数据
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            //获取子view的数量
            int childCount = getChildCount();
            //如果没有子view，返回
            if (childCount == 0) return;
            //进行测量
            mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
            //保存单行数据，当此行容不下新View时，新建一行
            List<View> line = null;
            lines.clear();
            //如果存在子view，进行测量
            for (int i = 0; i < childCount; i++) {
                View itemView = getChildAt(i);
                measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
                if (line == null) {
                    //说明当前行为空
                    line = createNewLine(itemView);
                } else {
                    //判断是否可以再继续添加
                    if (canBeAdd(itemView, line)) {
                        line.add(itemView);
                    } else {
                        line = createNewLine(itemView);
                    }
                }
            }
            mItemHeight = getChildAt(0).getMeasuredHeight();
            //测量自己
            int selfHeight = lines.size() * mItemHeight + mItemVerticalSpace * (line.size() + 1);
            setMeasuredDimension(mSelfWidth, selfHeight);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否还可以继续添加数据
     * @param itemView
     * @param line
     */
    private Boolean canBeAdd(View itemView, List<View> line) {
        //所有已经添加的的子View宽度相加 + (line.size() + 1) * mItemHorizontalSpace + itemView.getMeasureWidth()
        //如果结果小于/等于当前控件的宽度，则可以添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();
        //累加--已添加View的宽度
        for (View view : line) {
            totalWidth += view.getMeasuredWidth();
        }
        //累加--间距
        totalWidth += (line.size() + 1) * mItemVerticalSpace;
        return  totalWidth <= mSelfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //布局子View
        //遍历所有行
        int topOffset = mItemVerticalSpace;
        for (List<View> line : lines) {
            int leftOffset = mItemHorizontalSpace;
            for (View view : line) {
                //为第x行的View布局
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                //调整左侧偏移量
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mItemHeight + mItemVerticalSpace;
            //换行，调整顶部偏移量
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public int getContentSize() {
        return mTextList.size();
    }

    public interface OnFlowTextItemClickListener {
        void onFlowTextItemClick(int index, String text);
        void onFlowTextItemLongChick(int index, String text);
    }
}
