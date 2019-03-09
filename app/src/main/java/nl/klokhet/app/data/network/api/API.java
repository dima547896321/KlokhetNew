package nl.klokhet.app.data.network.api;

import java.util.List;

import io.reactivex.Single;
import nl.klokhet.app.data.network.model.request.ChekinUser;
import nl.klokhet.app.data.network.model.request.CurrentCheckinStatusesRequest;
import nl.klokhet.app.data.network.model.request.ForgorePasswordReqquest;
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
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {

    @POST("login")
    Single<BaseResponce<Status, LoginResponce>> login(@Body LoginRequest request);

    @POST("forgetPassword")
    Single<ForgotePasswordResponce> forgotePass(@Body ForgorePasswordReqquest request);

    @POST("privacyPolice")
    Single<BaseResponce<Status, PrivacyPolicy>> policy();

    @POST("currentSchedule")
    Single<BaseResponce<Status, LessonsResponce>> currentShedule();

    @POST("currentCheckinStatuses")
    Single<BaseResponce<Status, List<UserInGroup>>> getInfoOfGroup(@Body CurrentCheckinStatusesRequest request);

    @POST("currentTeacherProfile")
    Single<BaseResponce<Status, TeacherInfoResponce>> getTeacherInfo();

    @POST("scheduleContext")
    Single<BaseResponce<Status, SheduleContextResponce>> getscheduleContext();

    @POST("checkin")
    Single<BaseResponce<Status, List<String>>> checkin(@Body ChekinUser user);

    @POST("studentProfile")
    Single<BaseResponce<Status, UserProfileResponce>> getUserProfile(@Body ChekinUser user);

    @POST("saveSchedule")
    Single<BaseResponce<Status, List<UserInGroup>>> saveSchedule(@Body SaveScheduleRequest request);

    @POST("checkout")
    Single<BaseResponce<Status, List<String>>> checkOut(@Body ChekinUser user);


}
