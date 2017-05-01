package com.nullusera.loading;

import android.view.LayoutInflater;
import android.view.View;

import com.nullusera.loading.base.BaseActivity;
import com.nullusera.loading.widget.LoadingPage;

public class MainActivity extends BaseActivity {

    @Override
    protected LoadingPage.LoadResult load() {
//        return LoadingPage.LoadResult.SUCCESS;
        return LoadingPage.LoadResult.EMPTY;
//        return LoadingPage.LoadResult.ERROR;
    }

    @Override
    protected View createLoadedView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_main, null);
    }
}
