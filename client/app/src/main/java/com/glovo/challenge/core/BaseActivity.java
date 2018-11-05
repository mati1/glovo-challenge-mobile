package com.glovo.challenge.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    private P presenter;

    protected abstract P createPresenter();

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter.getView() == null) {
            presenter.attachView(this);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (presenter.getView() == null) {
            presenter.attachView(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    public P getPresenter() {
        return presenter;
    }
}
