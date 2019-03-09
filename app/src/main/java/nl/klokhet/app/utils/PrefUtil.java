package nl.klokhet.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import nl.klokhet.app.App;

public final class PrefUtil {

    private SharedPreferences mPreferences;

    private PrefUtil() {
        mPreferences = App
                .getInstance()
                .getApplicationContext()
                .getSharedPreferences(Contract.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PrefUtil getInstance() {
        return Loader.sInstance;
    }


    @SuppressLint("ApplySharedPref")
    public void cleanData() {
        mPreferences.edit()
                .remove(Contract.PREF_NAME)
                .remove(Contract.AUTO_LOGIN)
                .remove(Contract.USER_TOKEN)
                .remove(Contract.USER_EMAIL)
                .remove(Contract.USER_PASS)
                .commit();
    }


    private static final class Loader {
        private static final PrefUtil sInstance = new PrefUtil();

        private Loader() throws IllegalAccessException {
            throw new IllegalAccessException("Loader class");
        }
    }
    public static boolean getAutologin(){
        return PrefUtil.getInstance().mPreferences.getBoolean(Contract.AUTO_LOGIN, false);
    }
    public static void setAutologin(Boolean autologin){
        PrefUtil.getInstance().mPreferences.edit().putBoolean(Contract.AUTO_LOGIN, autologin).apply();
    }
    public static void setUserEmail(String userEmail){
        PrefUtil.getInstance().mPreferences.edit().putString(Contract.USER_EMAIL, userEmail).apply();
    }
    public static String getUserEmail(){
        return PrefUtil.getInstance().mPreferences.getString(Contract.USER_EMAIL, "");
    }
    public static void setUserPass(String userEmail){
        PrefUtil.getInstance().mPreferences.edit().putString(Contract.USER_PASS, userEmail).apply();
    }
    public static String getUserPass(){
        return PrefUtil.getInstance().mPreferences.getString(Contract.USER_PASS, "");
    }
    public static void setUserToken(String userToken){
        PrefUtil.getInstance().mPreferences.edit().putString(Contract.USER_TOKEN, userToken).apply();
    }
    public static String getUserToken(){
        return PrefUtil.getInstance().mPreferences.getString(Contract.USER_TOKEN, "");
    }
    public static void setFirstRequestCamera(Boolean userToken){
        PrefUtil.getInstance().mPreferences.edit().putBoolean(Contract.FIRST_REQEST_CAMERA, userToken).apply();
    }
    public static Boolean getFirstRequestCamera(){
        return PrefUtil.getInstance().mPreferences.getBoolean(Contract.FIRST_REQEST_CAMERA, false);
    }
    public static void setFirstRequestLocation(Boolean userToken){
        PrefUtil.getInstance().mPreferences.edit().putBoolean(Contract.FIRST_REQEST_LOCATION, userToken).apply();
    }
    public static Boolean getFirstRequestLocation(){
        return PrefUtil.getInstance().mPreferences.getBoolean(Contract.FIRST_REQEST_LOCATION, false);
    }
    private class Contract {
        public static final String PREF_NAME = "Klokhet";
        public static final String AUTO_LOGIN = "AUTO_LOGIN";
        public static final String USER_TOKEN = "USER_TOKEN";
        public static final String USER_EMAIL = "USER_EMAIL";
        public static final String USER_PASS = "USER_PASS";

        public static final String FIRST_REQEST_CAMERA = "FIRST_REQEST_CAMERA";
        public static final String FIRST_REQEST_LOCATION = "FIRST_REQEST_LOCATION";

    }


}
