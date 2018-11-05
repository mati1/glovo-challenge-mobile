package com.glovo.challenge.rules;

import android.support.annotation.NonNull;
import android.telecom.Call;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RxImmediateSchedulerRule implements TestRule {
    private Scheduler immediate = new Scheduler() {
        @Override
        public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
            // this prevents StackOverflowErrors when scheduling with a delay
            return super.scheduleDirect(run, 0, unit);
        }

        @Override
        public Worker createWorker() {
            return new ExecutorScheduler.ExecutorWorker(new Executor() {
                @Override
                public void execute(@NonNull final Runnable runnable) {
                    runnable.run();
                }
            });
        }
    };

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setInitIoSchedulerHandler(schedulerFunction);
                RxJavaPlugins.setInitComputationSchedulerHandler(schedulerFunction);
                RxJavaPlugins.setInitNewThreadSchedulerHandler(schedulerFunction);
                RxJavaPlugins.setInitSingleSchedulerHandler(schedulerFunction);
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerFunction);

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }

    Function<Callable<Scheduler>, Scheduler> schedulerFunction = new Function<Callable<Scheduler>, Scheduler>() {
        @Override
        public Scheduler apply(final Callable<Scheduler> schedulerCallable) throws Exception {
            return immediate;
        }
    };
}
