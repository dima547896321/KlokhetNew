package nl.klokhet.app.data.network.providers;

import java.util.List;

import io.reactivex.Single;
import nl.klokhet.app.data.network.NetworkModule;
import nl.klokhet.app.data.network.api.API;
import nl.klokhet.app.data.network.model.request.ChekinUser;
import nl.klokhet.app.data.network.model.request.CurrentCheckinStatusesRequest;
import nl.klokhet.app.data.network.model.request.ForgorePasswordReqquest;
import nl.klokhet.app.data.network.model.request.GPS;
import nl.klokhet.app.data.network.model.request.LoginRequest;
import nl.klokhet.app.data.network.model.request.SaveScheduleRequest;
import nl.klokhet.app.data.network.responce.BaseResponce;
import nl.klokhet.app.data.network.responce.ForgotePasswordResponce;
import nl.klokhet.app.data.network.responce.LessonsResponce;
import nl.klokhet.app.data.network.responce.LoginResponce;
import nl.klokhet.app.data.network.responce.PrivacyPolicy;
import nl.klokhet.app.data.network.responce.SheduleContextResponce;
import nl.klokhet.app.data.network.responce.Status;
import nl.klokhet.app.data.network.responce.TeacherInfoResponce;
import nl.klokhet.app.data.network.responce.UserInGroup;
import nl.klokhet.app.data.network.responce.UserProfileResponce;
import nl.klokhet.app.utils.NetworkErrorUtil;

public class UserProviderImpl {
    private API mBackendService;

    public UserProviderImpl() {
        initNetworkModule();
    }

    protected void initNetworkModule() {
        mBackendService = NetworkModule.getBackEndService();
    }

    public Single<BaseResponce<Status, LoginResponce>> login(String login, String pass) {
        return mBackendService.login(new LoginRequest(login, pass))
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<ForgotePasswordResponce> forgotePass(String email) {
        return mBackendService.forgotePass(new ForgorePasswordReqquest(email))
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, PrivacyPolicy>> policy() {
        return mBackendService.policy()
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, LessonsResponce>> currentShedule() {
        return mBackendService.currentShedule()
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, LessonsResponce>> getGroupInfo() {
        return mBackendService.currentShedule()
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, List<UserInGroup>>> getInfoOfGroup(String schedule, String groupId) {
        return mBackendService.getInfoOfGroup(new CurrentCheckinStatusesRequest(schedule, groupId))
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, TeacherInfoResponce>> getTeacherInfo() {
        return mBackendService.getTeacherInfo()
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, SheduleContextResponce>> getscheduleContext() {
        return mBackendService.getscheduleContext()
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, UserProfileResponce>> getUserProfile(String qr, String schedule) {
        ChekinUser user = new ChekinUser();
        user.setQrCode(qr);
        user.setScheduleId(schedule);
        return mBackendService.getUserProfile(user)
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }


    public Single<BaseResponce<Status, List<String>>> checkin(Double lat, Double lon, String qr, String schedule) {
        GPS gps = new GPS(String.valueOf(lat), String.valueOf(lon));
        ChekinUser user = new ChekinUser();
        user.setGps(gps);
        user.setQrCode(qr);
        user.setScheduleId(schedule);
        return mBackendService.checkin(user)
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, List<UserInGroup>>> saveShedule(String name, String lessonId, String lessonTypeId, String studentGroupId, String locationId, String dateTimeStart, String duration) {
        SaveScheduleRequest request = new SaveScheduleRequest();
        request.setName(name);
        request.setLessonId(lessonId);
        request.setLessonTypeId(lessonTypeId);
        request.setStudentGroupId(studentGroupId);
        request.setLocationId(locationId);
        request.setDateTimeStart(dateTimeStart);
        request.setDuration(duration);
        return mBackendService.saveSchedule(request)
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }

    public Single<BaseResponce<Status, List<String>>> checkOut( String qr, String schedule) {
        ChekinUser user = new ChekinUser();
        user.setQrCode(qr);
        user.setScheduleId(schedule);
        return mBackendService.checkOut(user)
                .onErrorResumeNext(NetworkErrorUtil.instance().rxParseErrorSingle());
    }
}
