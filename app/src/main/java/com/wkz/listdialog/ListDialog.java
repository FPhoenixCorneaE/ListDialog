package com.wkz.listdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 列表弹窗
 * Created by Administrator on 2018/5/8.
 */

public class ListDialog extends Dialog {

    private Builder mBuilder;
    private View mContentView;

    public ListDialog(Builder mBuilder) {
        super(mBuilder.mContext);
        this.mBuilder = mBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化窗口属性
        initWindowAttribute();

        //设置视图
        setContentView(mContentView = getContentView());

        //设置是否可撤销
        setCancelable(mBuilder.mCancelable);

        //设置触摸外部是否可撤销
        setCanceledOnTouchOutside(mBuilder.mCanceledOnTouchOutside);

    }

    @SuppressWarnings("ConstantConditions")
    private void initWindowAttribute() {
        //替换掉默认主题的背景，默认主题背景有一个16dp的padding
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //外边距
        ViewUtils.setViewPadding(getWindow().getDecorView(), true, mBuilder.mDialogMarginLeft, mBuilder.mDialogMarginTop, mBuilder.mDialogMarginRight, mBuilder.mDialogMarginBottom);

        //设置重力位置
        getWindow().setGravity(mBuilder.mDialogGravity);

        //设置弹窗的宽高
        getWindow().setLayout(mBuilder.mDialogWidth, mBuilder.mDialogHeight);
    }

    @NonNull
    private View getContentView() {

        //线性布局容器
        ViewGroup.LayoutParams containerLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout containerView = new LinearLayout(mBuilder.mContext);
        containerView.setLayoutParams(containerLayoutParams);

        //方向，固定为垂直方向
        containerView.setOrientation(LinearLayout.VERTICAL);

        //容器背景
        ViewCompat.setBackground(containerView, mBuilder.mBackgroundDrawable);

        //分割线
        containerView.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        containerView.setDividerDrawable(mBuilder.mDividerDrawable);


        //添加item
        ViewGroup.LayoutParams itemLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mBuilder.mItemHeight);

        for (int i = 0; i < mBuilder.mDatas.size(); i++) {

            TextView itemView = new TextView(mBuilder.mContext);

            itemView.setLayoutParams(itemLayoutParams);
            itemView.setGravity(mBuilder.mTextGravity);
            itemView.setTextSize(mBuilder.mTextSize);
            itemView.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_selected}, {-android.R.attr.state_selected}},
                    new int[]{mBuilder.mTextSelectedColor, mBuilder.mTextNormalColor}));
            itemView.setText(mBuilder.mDatas.get(i));

            //内边距
            ViewUtils.setViewPadding(itemView, true, mBuilder.mTextPaddingLeft, mBuilder.mTextPaddingTop, mBuilder.mTextPaddingRight, mBuilder.mTextPaddingBottom);

            //选中项
            if (i == mBuilder.mSelectedPosition) {
                itemView.setSelected(true);
            }

            //item点击
            final int finalI = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < containerView.getChildCount(); j++) {
                        containerView.getChildAt(j).setSelected(false);
                    }

                    containerView.getChildAt(finalI).setSelected(true);
                    mBuilder.setSelectedPosition(finalI);

                    if (mBuilder.mOnItemClickListener != null) {
                        mBuilder.mOnItemClickListener.onItemClick(ListDialog.this, mBuilder.mDatas, finalI);
                    }

                    dismiss();
                }
            });

            containerView.addView(itemView);
        }
        return containerView;
    }

    @Override
    public void show() {
        super.show();
        if (mContentView != null && mBuilder.mDialogExitAnimation != null) {
            mContentView.startAnimation(mBuilder.mDialogEnterAnimation);
        }
    }

    @Override
    public void dismiss() {

        if (mBuilder.mDialogExitAnimation != null) {

            mBuilder.mDialogExitAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mContentView != null) {
                        //用view的post()方法不会报错，直接用super.dismiss()会报错的，你可以试试。
                        mContentView.post(new Runnable() {
                            @Override
                            public void run() {
                                ListDialog.super.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            if (mContentView != null) {
                mContentView.startAnimation(mBuilder.mDialogExitAnimation);
            }
        } else {
            super.dismiss();
        }
    }

    public static class Builder {

        private Context mContext;
        private List<String> mDatas;
        private boolean mCancelable;
        private boolean mCanceledOnTouchOutside;
        private int mDialogWidth;
        private int mDialogHeight;
        private Drawable mBackgroundDrawable;
        private Drawable mDividerDrawable;
        private float mBackgroundCornerRadius;
        private @ColorInt
        int mBackgroundColor;
        private int mDialogGravity;
        private float mDialogMarginLeft;
        private float mDialogMarginTop;
        private float mDialogMarginRight;
        private float mDialogMarginBottom;
        private Animation mDialogEnterAnimation;
        private Animation mDialogExitAnimation;
        private @ColorInt
        int mTextNormalColor;
        private @ColorInt
        int mTextSelectedColor;
        private float mTextSize;
        private int mTextGravity;
        private float mTextPaddingLeft;
        private float mTextPaddingTop;
        private float mTextPaddingRight;
        private float mTextPaddingBottom;
        private int mItemHeight;
        private int mSelectedPosition;
        private OnItemClickListener mOnItemClickListener;

        public Builder(@NonNull Context mContext) {
            this.mContext = mContext;

            //属性默认赋值
            this.mCancelable = true;
            this.mCanceledOnTouchOutside = true;
            this.mDialogWidth = -1;//ViewGroup.LayoutParams.MATCH_PARENT
            this.mDialogHeight = -2;//ViewGroup.LayoutParams.WRAP_CONTENT
            this.mBackgroundCornerRadius = 5f;
            this.mBackgroundColor = Color.WHITE;
            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
            backgroundDrawable.setCornerRadius(ViewUtils.dp2px(mContext, mBackgroundCornerRadius));
            backgroundDrawable.setColor(mBackgroundColor);
            this.mBackgroundDrawable = backgroundDrawable;
            this.mDividerDrawable = new ColorDrawable(Color.parseColor("#FFEDEDED"));
            this.mDialogGravity = Gravity.CENTER;
            this.mDialogMarginLeft = 15f;
            this.mDialogMarginTop = 5f;
            this.mDialogMarginRight = 15f;
            this.mDialogMarginBottom = 5f;
            this.mTextNormalColor = Color.parseColor("#FF505050");
            this.mTextSelectedColor = Color.parseColor("#FFFE5848");
            this.mTextSize = 13f;
            this.mTextGravity = Gravity.CENTER;
            this.mItemHeight = ViewUtils.dp2px(mContext, 48f);
        }

        public Builder setDatas(ArrayList<String> mDatas) {
            this.mDatas = mDatas == null ? new ArrayList<String>() : mDatas;
            return this;
        }

        public Builder setDatas(String[] mDatas) {
            this.mDatas = mDatas == null ? new ArrayList<String>() : Arrays.asList(mDatas);
            return this;
        }

        public Builder setCancelable(boolean mCancelable) {
            this.mCancelable = mCancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean mCanceledOnTouchOutside) {
            this.mCanceledOnTouchOutside = mCanceledOnTouchOutside;
            return this;
        }

        public Builder setDialogWidth(int mDialogWidth) {
            this.mDialogWidth = mDialogWidth;
            return this;
        }

        public Builder setDialogHeight(int mDialogHeight) {
            this.mDialogHeight = mDialogHeight;
            return this;
        }

        public Builder setDialogBackground(@ColorInt int mBackgroundColor, float mBackgroundCornerRadius) {
            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
            backgroundDrawable.setCornerRadius(ViewUtils.dp2px(mContext, mBackgroundCornerRadius));
            backgroundDrawable.setColor(mBackgroundColor);
            this.mBackgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder setDialogBackground(Drawable mBackgroundDrawable) {
            if (mBackgroundDrawable != null) {
                this.mBackgroundDrawable = mBackgroundDrawable;
            }
            return this;
        }

        public Builder setDividerDrawable(Drawable mDividerDrawable) {
            if (mDividerDrawable != null) {
                this.mDividerDrawable = mDividerDrawable;
            }
            return this;
        }

        public Builder setDialogGravity(int mDialogGravity) {
            this.mDialogGravity = mDialogGravity;
            return this;
        }

        public Builder setDialogMargins(float leftDpValue, float topDpValue, float rightDpValue, float bottomDpValue) {
            this.mDialogMarginLeft = leftDpValue;
            this.mDialogMarginTop = topDpValue;
            this.mDialogMarginRight = rightDpValue;
            this.mDialogMarginBottom = bottomDpValue;
            return this;
        }

        public Builder setDialogEnterAnimation(Animation mDialogEnterAnimation) {
            if (mDialogEnterAnimation != null) {
                this.mDialogEnterAnimation = mDialogEnterAnimation;
            }
            return this;
        }

        public Builder setDialogEnterAnimation(@AnimRes int mDialogEnterAnimationRes) {
            try {
                this.mDialogEnterAnimation = AnimationUtils.loadAnimation(mContext, mDialogEnterAnimationRes);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder setDialogExitAnimation(Animation mDialogExitAnimation) {
            if (mDialogExitAnimation != null) {
                this.mDialogExitAnimation = mDialogExitAnimation;
            }
            return this;
        }

        public Builder setDialogExitAnimation(@AnimRes int mDialogExitAnimationRes) {
            try {
                this.mDialogExitAnimation = AnimationUtils.loadAnimation(mContext, mDialogExitAnimationRes);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder setTextNormalColor(@ColorInt int mTextNormalColor) {
            this.mTextNormalColor = mTextNormalColor;
            return this;
        }

        public Builder setTextSelectedColor(@ColorInt int mTextSelectedColor) {
            this.mTextSelectedColor = mTextSelectedColor;
            return this;
        }

        public Builder setTextSize(float spValue) {
            this.mTextSize = spValue;
            return this;
        }

        public Builder setTextGravity(int mTextGravity) {
            this.mTextGravity = mTextGravity;
            return this;
        }

        public Builder setTextPadding(float leftDpValue, float topDpValue, float rightDpValue, float bottomDpValue) {
            this.mTextPaddingLeft = leftDpValue;
            this.mTextPaddingTop = topDpValue;
            this.mTextPaddingRight = rightDpValue;
            this.mTextPaddingBottom = bottomDpValue;
            return this;
        }

        public Builder setItemHeight(float dpValue) {
            if (dpValue > 0) {
                this.mItemHeight = ViewUtils.dp2px(mContext, dpValue);
            }
            return this;
        }

        public Builder setSelectedPosition(int mSelectedPosition) {
            this.mSelectedPosition = mSelectedPosition;
            return this;
        }

        public Builder setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
            return this;
        }

        public ListDialog build() {
            return new ListDialog(this);
        }

        public void show() {
            new ListDialog(this).show();
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Dialog dialog, List<String> datas, int position);

    }
}
