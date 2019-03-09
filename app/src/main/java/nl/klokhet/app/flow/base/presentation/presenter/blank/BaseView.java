package nl.klokhet.app.flow.base.presentation.presenter.blank;

import android.support.annotation.StringRes;
import android.view.View;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface BaseView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void onError(String error);

    @StateStrategyType(SkipStrategy.class)
    void onHideKeyBoard(String error);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showProgress();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void hideProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showAlert(@StringRes int message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showAlert(String message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showAlert(String message, String title);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSnackBar(@StringRes int res);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSnackBar(String text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSnackBar(View rootView, @StringRes int res);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSnackBar(View rootView, String text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void backPress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showToast(String message);
}
