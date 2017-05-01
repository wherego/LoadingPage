package com.nullusera.loading.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nullusera.loading.R;
import com.nullusera.loading.widget.LoadingPage;

/**
 * Author: renhe on 2017/5/1.
 * Email:  renhe228@gmail.com
 * Des:    Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected LoadingPage mPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPage == null) {
            mPage = new LoadingPage(this, R.layout.loading_page_loading, R.layout.loading_page_empty,
                    R.layout.loading_page_error) {
                @Override
                public View createLoadedView() {
                    return BaseActivity.this.createLoadedView();
                }

                @Override
                public LoadResult load() {
                    return BaseActivity.this.load();
                }
            };
        }
        setContentView(mPage);
        mPage.show();
    }

    /**
     * 请求网络数据
     */
    protected abstract LoadingPage.LoadResult load();

    /**
     * 数据请求成功自定义显示页面
     */
    protected abstract View createLoadedView();
}

