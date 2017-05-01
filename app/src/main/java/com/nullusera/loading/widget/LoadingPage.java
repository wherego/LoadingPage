package com.nullusera.loading.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.nullusera.loading.R;

/**
 * Author: renhe on 2017/5/1.
 * Email:  renhe228@gmail.com
 * Des:
 */

public abstract class LoadingPage extends FrameLayout {
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR   = 2;
    private static final int STATE_EMPTY   = 3;
    private static final int STATE_SUCCESS = 4;

    /**
     * 加载中显示的页面
     */
    private View mLoadingView;
    /**
     * 请求出错显示的页面
     */
    private View mErrorView;
    /**
     * 请求成功但是没有数据时显示的页面
     */
    private View mEmptyView;
    /**
     * 请求成功显示的页面
     */
    private View mSucceedView;

    /**
     * 当前状态(决定当前显示的页面)
     */
    private int mCurrentState;

    /**
     * 加载中布局id
     */
    private int mLoadingLayoutId;

    /**
     * 加载出错布局id
     */
    private int mErrorLayoutId;

    /**
     * 空数据布局id
     */
    private int mEmptyLayoutId;

    /**
     * 构造函数
     *
     * @param var1 {@link Context}
     * @param var2 加载中页面对应布局id
     * @param var3 空数据时对应布局id
     * @param var4 错误页面对应布局id
     */
    public LoadingPage(Context var1, int var2, int var3, int var4) {
        super(var1);
        initLayout(var2, var3, var4);
        init(var1);
    }

    /**
     * 初始化各种状态页面布局
     */
    private void initLayout(int var1, int var2, int var3) {
        this.mLoadingLayoutId = var1;
        this.mEmptyLayoutId = var2;
        this.mErrorLayoutId = var3;
    }

    /**
     * 创建不同状态的页面
     *
     * @param context {@link Context}
     */
    private void init(Context context) {
        mCurrentState = STATE_DEFAULT;
        setBackgroundColor(getResources().getColor(R.color.pagePrimary));

        mLoadingView = createLoadingView(context);
        if (mLoadingView != null) {
            addView(mLoadingView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }

        mEmptyView = createEmptyView(context);
        if (mEmptyView != null) {
            addView(mEmptyView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }

        mErrorView = createErrorView(context);
        if (mErrorView != null) {
            addView(mErrorView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }

        showPage();
    }

    /**
     * 根据状态显示对应的页面
     */
    private void showPage() {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(mCurrentState == STATE_DEFAULT || mCurrentState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
        }

        if (mCurrentState == STATE_SUCCESS && mSucceedView == null) {
            mSucceedView = createLoadedView();
            addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        if (null != mSucceedView) {
            mSucceedView.setVisibility(mCurrentState == STATE_SUCCESS ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 显示对应的页面
     */
    public synchronized void show() {
        if (mCurrentState == STATE_EMPTY || mCurrentState == STATE_ERROR) {
            mCurrentState = STATE_DEFAULT;
        }

        if (mCurrentState == STATE_DEFAULT) {
            mCurrentState = STATE_LOADING;
            new Thread(new LoadingTask()).start();
        }

        showPage();
    }

    private class LoadingTask implements Runnable {
        @Override
        public void run() {
            LoadResult loadResult = load();
            mCurrentState = loadResult.getResult();
            showPage();
        }
    }

    /**
     * 恢复默认状态
     */
    public void setDefault() {
        mCurrentState = STATE_DEFAULT;
        showPage();
    }

    /**
     * 数据请求成功后页面具体的显示
     */
    public abstract View createLoadedView();

    public abstract LoadResult load();

    private View inflateLayout(Context context, int layoutId) {
        return View.inflate(context, layoutId, null);
    }

    /**
     * 创建错误页面
     */
    private View createErrorView(Context context) {
        View errorView = inflateLayout(context, mErrorLayoutId);
        // 加载出错，点击重试
        errorView.findViewById(R.id.page_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        return errorView;
    }

    /**
     * 创建空数据页面
     */
    private View createEmptyView(Context context) {
        return inflateLayout(context, mEmptyLayoutId);
    }

    /**
     * 创建加载页面
     */
    private View createLoadingView(Context context) {
        return inflateLayout(context, mLoadingLayoutId);
    }

    public enum LoadResult {
        ERROR(2), EMPTY(3), SUCCESS(4);

        private int result;

        LoadResult(int result) {
            this.result = result;
        }

        public int getResult() {
            return result;
        }
    }
}

