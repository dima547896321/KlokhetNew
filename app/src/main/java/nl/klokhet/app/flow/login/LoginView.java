package nl.klokhet.app.flow.login;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import nl.klokhet.app.flow.base.presentation.presenter.blank.BaseView;

public interface LoginView extends BaseView {

    @StateStrategyType(SkipStrategy.class)
    void goToNextActivity();
}
