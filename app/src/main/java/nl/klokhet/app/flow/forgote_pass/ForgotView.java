package nl.klokhet.app.flow.forgote_pass;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import nl.klokhet.app.flow.base.presentation.presenter.blank.BaseView;

public interface ForgotView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showToastWithText(String message);
}
