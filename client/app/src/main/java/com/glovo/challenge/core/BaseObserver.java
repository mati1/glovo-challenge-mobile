package com.glovo.challenge.core;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    public abstract void onSuccess(T response);

    public abstract void onError();

    @Override
    public final void onSubscribe(final Disposable d) {
        // Nothing to do
    }

    @Override
    public final void onNext(final T t) {
        onSuccess(t);
    }

    @Override
    public final void onError(final Throwable e) {
        onError();
    }

    @Override
    public final void onComplete() {
        // Nothing to do
    }
}
