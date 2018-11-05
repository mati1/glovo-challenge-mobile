package com.glovo.challenge.core;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

public abstract class BasePresenter<V extends BaseView> {

    @Nullable
    private V mView;

    @UiThread
    public void attachView(@NonNull final V view) {
        mView = view;
        onViewAttached(view);
    }

    @UiThread
    public void detachView() {
        mView = null;
        onViewDetached(mView);
    }

    @Nullable
    @UiThread
    @CheckResult
    protected V getView() {
        return mView;
    }

    protected abstract void onViewAttached(BaseView view);

    protected abstract void onViewDetached(BaseView view);
}
