package nl.klokhet.app.flow.main;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.google.gson.JsonParseException;

import java.util.List;

import io.reactivex.functions.Consumer;
import nl.klokhet.app.data.network.providers.ProviderModule;
import nl.klokhet.app.data.network.responce.BaseResponce;
import nl.klokhet.app.data.network.responce.ItemContextResponce;
import nl.klokhet.app.data.network.responce.LessonsResponce;
import nl.klokhet.app.data.network.responce.SheduleContextResponce;
import nl.klokhet.app.data.network.responce.Status;
import nl.klokhet.app.data.network.responce.TeacherInfoResponce;
import nl.klokhet.app.data.network.responce.UserInGroup;
import nl.klokhet.app.flow.base.presentation.presenter.blank.BasePresenter;
import nl.klokhet.app.utils.RxUtils;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    private Consumer<BaseResponce<Status, List<UserInGroup>>> doneGroupCheckin = done1 -> {
        hideProgress();
        getViewState().showGroupInfo(done1.getData(), done1.getStatus().getMessage());
    };
    private Consumer<BaseResponce<Status, TeacherInfoResponce>> teacher = done1 -> {
        hideProgress();
        getViewState().showTeacherInfo(done1.getData());
    };
    private Consumer<BaseResponce<Status, SheduleContextResponce>> scheduleContext = done1 -> {
        hideProgress();
        done1.getData().getLesson().add(0, new ItemContextResponce(-1, "Choose lesson"));
        done1.getData().getLessonType().add(0, new ItemContextResponce(-1, "Choose lesson type"));
        done1.getData().getGroup().add(0, new ItemContextResponce(-1, "Choose group"));
        done1.getData().getLocation().add(0, new ItemContextResponce(-1, "Choose locations"));
        getViewState().showSheduleContext(done1.getData());
    };
    private Consumer<BaseResponce<Status, LessonsResponce>> done = done1 -> {
        hideProgress();
        getViewState().showLessonsInfo(done1.getData());
        if (done1.getData() != null && done1.getData().getCurrent() != null) {
            getGroupInfo(
                    String.valueOf(done1.getData().getCurrent().getId()),
                    String.valueOf(done1.getData().getCurrent().getGroupId()));
        }
        getTeacherInfo();
    };
    private Consumer<BaseResponce<Status, List<UserInGroup>>> createNewSchedule = done1 -> {
        hideProgress();
        if (done1.getStatus().getSuccess()) {
            getViewState().createSchedule();
        } else {
            getViewState().showToast(done1.getStatus().getMessage());
        }
    };


    public void startTrack() {
        clearSubscription();

    }

    void getCurrentSchedule() {
        checkNetworkAndAddDisposable(false,
                ProviderModule.getUserProvider().currentShedule()
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(done, error -> {
                                    hideProgress();
                                    Timber.d("Error : " + error.getMessage());
                                    if (error instanceof JsonParseException) {
                                        LessonsResponce lessonsResponce = new LessonsResponce();
                                        done.accept(new BaseResponce<>(null, lessonsResponce));
                                    }
                                }
                        ));
    }

    private void getGroupInfo(String schedule, String group) {
        checkNetworkAndAddDisposable(false,
                ProviderModule.getUserProvider().getInfoOfGroup(schedule, group)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(doneGroupCheckin, error -> {
                                    hideProgress();
                                    Timber.d("Error: " + error.getMessage());
                                }
                        ));
    }

    private void getTeacherInfo() {
        checkNetworkAndAddDisposable(false,
                ProviderModule.getUserProvider().getTeacherInfo()
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(teacher, error -> {
                                    hideProgress();
                            Timber.d(error.getMessage());
                                }
                        ));
    }

    void getSheduleContext() {
        checkNetworkAndAddDisposable(true,
                ProviderModule.getUserProvider().getscheduleContext()
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(scheduleContext, error -> {
                                    hideProgress();
                            Timber.d(error.getMessage());
                                }
                        ));
    }

    void saveSchedule(String name, String lessonId, String lessonTypeId, String studentGroupId, String locationId, String dateTimeStart, String duration) {
        checkNetworkAndAddDisposable(true,
                ProviderModule.getUserProvider().saveShedule(name, lessonId, lessonTypeId, studentGroupId, locationId, dateTimeStart, duration)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(createNewSchedule, error -> {
                            hideProgress();
                            Timber.d(error.getMessage());
                        }));
    }
}
