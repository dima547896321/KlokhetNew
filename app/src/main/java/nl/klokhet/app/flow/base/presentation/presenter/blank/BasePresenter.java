package nl.klokhet.app.flow.base.presentation.presenter.blank;

import android.support.annotation.StringRes;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.utils.NetworkUtils;

public class BasePresenter<T extends BaseView> extends MvpPresenter<T> {
    protected static final int PAGE_START = 0;
    private static final String TAG = BasePresenter.class.getSimpleName();
    private static final int DEFAULT_DELAY_SHOW_PROGRESS = 300;
    private static final Gson gson = new Gson();
    public static int PER_PAGE = 20;

    private CompositeDisposable mBackgroundDisposable = new CompositeDisposable();

    protected void onError(Throwable throwable) {

        getViewState().hideProgress();
        getViewState().onError(throwable.getMessage());
    }

    protected void clearSubscription() {
        getViewState().hideProgress();
        if (mBackgroundDisposable != null && !mBackgroundDisposable.isDisposed()) {
            mBackgroundDisposable.dispose();
            mBackgroundDisposable = null;
        }
    }

    @Override
    public void attachView(T view) {
        super.attachView(view);
        if(mBackgroundDisposable==null){
            mBackgroundDisposable = new CompositeDisposable();
        }
    }

    public void addBackgroundDisposable(Disposable disposable) {
        mBackgroundDisposable.add(disposable);
    }

    protected void showProgress() {
        getViewState().showProgress();
    }

    protected void hideProgress() {
        getViewState().hideProgress();
    }

    protected String getString(@StringRes int strId) {
        return App.getInstance().getString(strId);
    }

    protected void checkNetworkAndAddDisposable(boolean withProgress,
                                                Disposable func) {
        if (mBackgroundDisposable == null || mBackgroundDisposable.isDisposed()) {
            Log.d(this.getClass().getSimpleName(), "mBackgroundDisposable is empty");
            mBackgroundDisposable = new CompositeDisposable();
        }
        if (NetworkUtils.isConnected(App.getInstance())) {
            if (withProgress) {
                showProgress();
            }
            addBackgroundDisposable(func);
        } else {
            hideProgress();
            getViewState().onError(getString(R.string.no_internet_connection));
        }
    }

    protected enum CustomError {
        LOGIN, EDIT_PROFILE, FORGOT_PASSWORD
    }
}
