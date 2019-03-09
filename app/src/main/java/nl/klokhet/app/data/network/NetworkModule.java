package nl.klokhet.app.data.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import nl.klokhet.app.App;
import nl.klokhet.app.BuildConfig;
import nl.klokhet.app.data.network.api.API;
import nl.klokhet.app.data.network.responce.DateTimeStart;
import nl.klokhet.app.data.network.responce.Lesson;
import nl.klokhet.app.data.network.responce.LessonsResponce;
import nl.klokhet.app.data.network.responce.UserInGroup;
import nl.klokhet.app.flow.login.LoginActivity;
import nl.klokhet.app.utils.PrefUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {
    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm aa";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_ANDROID = "x-android";
    private static volatile Retrofit sRetrofit;

    private static OkHttpClient generateOkHttpClient() {
        HttpLoggingInterceptorHeavyFilesSafe httpLoggingInterceptor = new HttpLoggingInterceptorHeavyFilesSafe();
        HttpLoggingInterceptorHeavyFilesSafe.Level logLevel = BuildConfig.DEBUG ?
                HttpLoggingInterceptorHeavyFilesSafe.Level.BODY : HttpLoggingInterceptorHeavyFilesSafe.Level.NONE;
        httpLoggingInterceptor.setLevel(logLevel);
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder();
                    requestBuilder.method(original.method(), original.body());
                    return chain.proceed(addHeader(requestBuilder));
                })
                .addInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    if (response.code() == NetworkContract.HttpStatus.UNAUTHORIZED
                            || response.code() == NetworkContract.HttpStatus.FORBIDDEN) {
                        LoginActivity.start(App.getInstance(), true);
                    }
                    return response;
                })
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cache(null)
                .build();
    }

    private static Retrofit getRetrofit() {
        JsonDeserializer<LessonsResponce> lessonJsonDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            Gson gson = new Gson();

            LessonsResponce lessonresponse = new LessonsResponce();
            if (jsonObject.has("token")) {
                lessonresponse.setToken(jsonObject.get("token").getAsString());
            }
            if (jsonObject.has("current")) {
                if (jsonObject.get("current").isJsonArray()) {
                    lessonresponse.setCurrent(null);
                } else {
                    Lesson l = gson.fromJson(jsonObject.get("current"), Lesson.class);
                    lessonresponse.setCurrent(l);
                }
            }
            return lessonresponse;
        };
        JsonDeserializer<UserInGroup> lessonJsonDeserializer2 = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            Gson gson = new Gson();

            UserInGroup userProfileResponce = new UserInGroup();
            if (jsonObject.has("id")) {
                userProfileResponce.setId(jsonObject.get("id").getAsInt());
            }
            if (jsonObject.has("first_name")) {
                userProfileResponce.setFirst_name(jsonObject.get("first_name").getAsString());
            }
            if (jsonObject.has("last_name")) {
                userProfileResponce.setLast_name(jsonObject.get("last_name").getAsString());
            }
            if (jsonObject.has("email")) {
                userProfileResponce.setEmail(jsonObject.get("email").getAsString());
            }
            if (jsonObject.has("status")) {
                userProfileResponce.setStatus(jsonObject.get("status").getAsString());
            }
            if (jsonObject.has("photo")) {
                userProfileResponce.setPhoto(jsonObject.get("photo").getAsString());
            }
            if (jsonObject.has("time_in")) {
                userProfileResponce.setTime_in(jsonObject.get("time_in").getAsString());
            }
            if (jsonObject.has("time_out")) {
                userProfileResponce.setTime_out(jsonObject.get("time_out").getAsString());
            }
            if (jsonObject.has("radius")) {
                userProfileResponce.setRadius(jsonObject.get("radius").getAsString());
            }
            if (jsonObject.has("time")) {
                if (jsonObject.get("time").isJsonObject()) {
                    DateTimeStart g = gson.fromJson(jsonObject.get("time"), DateTimeStart.class);
                    userProfileResponce.setTime(g);
                } else {
                    userProfileResponce.setTimeExpire(jsonObject.get("time").getAsString());
                    DateTimeStart d = new DateTimeStart();
                    d.setDate(jsonObject.get("time").getAsString().concat(".000"));
                    userProfileResponce.setTime(d);
                }
            }

            return userProfileResponce;
        };
        Gson gson = new GsonBuilder().setDateFormat(DATE_TIME_FORMAT)
                .serializeNulls()
                .registerTypeAdapter(LessonsResponce.class, lessonJsonDeserializer)
                .registerTypeAdapter(UserInGroup.class, lessonJsonDeserializer2)
                .create();
        return new Retrofit.Builder()
                .baseUrl("https://klokhet.sapient.pro/api/")
                .client(generateOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static API getBackEndService() {
        return getRetrofit().create(API.class);
    }

    private static Request addHeader(Request.Builder requestBuilder) {
        requestBuilder.removeHeader(HEADER_AUTHORIZATION);
        requestBuilder.removeHeader(HEADER_ANDROID);
        if (!TextUtils.isEmpty(PrefUtil.getUserToken())) {
            requestBuilder.addHeader(HEADER_AUTHORIZATION, "Bearer " + PrefUtil.getInstance().getUserToken());
        }
        requestBuilder.addHeader(HEADER_ANDROID, "1");
        return requestBuilder.build();
    }
}
