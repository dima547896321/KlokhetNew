package nl.klokhet.app.flow.main;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import nl.klokhet.app.data.network.responce.LessonsResponce;
import nl.klokhet.app.data.network.responce.SheduleContextResponce;
import nl.klokhet.app.data.network.responce.TeacherInfoResponce;
import nl.klokhet.app.data.network.responce.UserInGroup;
import nl.klokhet.app.flow.base.presentation.presenter.blank.BaseView;

public interface MainView extends BaseView {

    @StateStrategyType(SkipStrategy.class)
    void goToNextActivity();

    @StateStrategyType(SkipStrategy.class)
    void showLessonsInfo(LessonsResponce lessonsResponce);

    @StateStrategyType(SkipStrategy.class)
    void showGroupInfo(List<UserInGroup> list, String message);

    @StateStrategyType(SkipStrategy.class)
    void showTeacherInfo(TeacherInfoResponce teacher);

    @StateStrategyType(SkipStrategy.class)
    void showSheduleContext(SheduleContextResponce teacher);

    @StateStrategyType(SkipStrategy.class)
    void reloadInfo();

    @StateStrategyType(SkipStrategy.class)
    void createSchedule();
}
