package nl.klokhet.app.flow.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.data.network.responce.LessonsResponce;
import nl.klokhet.app.data.network.responce.SheduleContextResponce;
import nl.klokhet.app.data.network.responce.TeacherInfoResponce;
import nl.klokhet.app.data.network.responce.UserInGroup;
import nl.klokhet.app.flow.base.ui.blank.BaseActivity;
import nl.klokhet.app.flow.login.LoginActivity;
import nl.klokhet.app.flow.main.tabs.ListFragment;
import nl.klokhet.app.flow.main.tabs.ScannerFragmentGoogleVisionFragment;
import nl.klokhet.app.flow.main.tabs.SettingsMainFragment;
import nl.klokhet.app.ui.NonScrollableViewPager;
import nl.klokhet.app.utils.RxUtils;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainView, LocationListener {
    public final static int TIK_TIME = 10;
    public static LessonsResponce lessonsResponce;
    public static SheduleContextResponce shedulerContext;
    public static Location location;
    public LocationManager locationManager;
    @InjectPresenter
    MainPresenter mLoginPresenter;
    NonScrollableViewPager viewPager;
    TabLayout tabLayout;
    CountDownTimer timer;
    AlertDialog dialog;
    Disposable d;
    private FragmentsViewPagerAdapter pagerAdapter;
    private List<UserInGroup> groupList;

    public static void start(Context context) {
        start(context, true);
    }

    public static void start(Context context, boolean clearStack) {
        Intent intent = new Intent(context, MainActivity.class);
        if (clearStack) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main_tabs);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(App.getInstance().getResources().getColor(R.color.white));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initMainProgressBar();
        viewPager = findViewById(R.id.viewpager);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "PT_Sans-Web-Bold.ttf");
        pagerAdapter = new FragmentsViewPagerAdapter(getSupportFragmentManager(), typeface, this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                highLightCurrentTab(i);
                if (i == 0 || i == 1) {
                    showLessonsInfo(lessonsResponce);
                }
                if (i == 0) {
                    ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).tabSelected();
                } else {
                    ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).tabUnSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        highLightCurrentTab(0);
        tabLayout.getTabAt(0).select();
        ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).tabSelected();
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(pagerAdapter.getSelectedTabView(position));
    }

    private void startTraking() {
        d = Observable.interval(TIK_TIME, TimeUnit.SECONDS)
                .compose(RxUtils.ioToMainTransformer())
                .subscribe(done -> {
                    mLoginPresenter.getCurrentSchedule();
                }, error -> {
                    Timber.d(error.getMessage());
                });
        mLoginPresenter.getCurrentSchedule();
    }

    private void startTimerToEndLessons() {

    }

    @Override
    public void goToNextActivity() {
        finish();
    }

    @Override
    public void reloadInfo() {
        mLoginPresenter.getCurrentSchedule();
    }

    @Override
    protected void onPause() {
        if (d != null && !d.isDisposed()) {
            d.dispose();
            d = null;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTraking();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LoginActivity.start(this, true);
            finish();
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0f, this);
//            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//            Location location1 = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//            onLocationChanged(location);
//            onLocationChanged(location1);
//            if (!locationManager.isLocationEnabled()) {
//                showToast("Please, enable GPS locator");
//            }
            ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).onResume();
            if (viewPager.getCurrentItem() == 2) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            mLoginPresenter.getSheduleContext();
        }
    }


    public void updateUserInList(UserInGroup user) {
        ((ListFragment) pagerAdapter.getItem(1)).updateUser(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).onResume();
    }

    @Override
    public void showLessonsInfo(LessonsResponce mLessonsResponce) {
        lessonsResponce = mLessonsResponce;
        if (lessonsResponce != null && lessonsResponce.getCurrent() != null) {
            ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).showCurrentLesson(lessonsResponce.getCurrent());

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            ((ListFragment) pagerAdapter.getItem(1)).showCurrentLesson(null);
            ((ListFragment) pagerAdapter.getItem(1)).showGroupInfo(new ArrayList<>());
            ((SettingsMainFragment) pagerAdapter.getItem(2)).bindTeacher();
            ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).showCurrentLesson(null);
            if (dialog == null || !dialog.isShowing() && viewPager.getCurrentItem() != 2) {
                showDialogNoLeasson();
            }
        }
    }

    public void showDialogNoLeasson() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_no_leasson, null);

        builder.setView(dialogView);
        // Get the custom alert dialog view widgets reference
        Button btn_positive = dialogView.findViewById(R.id.btnRefresh);
        Button btn_negative = dialogView.findViewById(R.id.btnGoToManualMode);
        btn_positive.setTypeface(Typeface.createFromAsset(getAssets(), "PT_Sans-Web-Bold.ttf"));
        btn_negative.setTypeface(Typeface.createFromAsset(getAssets(), "PT_Sans-Web-Bold.ttf"));
        // Create the alert dialog
        dialog = builder.create();
        dialog.setCancelable(false);
        btn_positive.setOnClickListener(v -> {
            reloadInfo();
            dialog.cancel();
        });

        btn_negative.setOnClickListener(v -> {
            // Dismiss the alert dialog
            goToManualMode();
            dialog.cancel();
        });

        dialog.show();
    }

    @Override
    public void showGroupInfo(List<UserInGroup> list, String message) {
        this.groupList = list;
        if (lessonsResponce != null && lessonsResponce.getCurrent() != null) {
            ((ListFragment) pagerAdapter.getItem(1)).showCurrentLesson(lessonsResponce.getCurrent());
            if (list == null) {
                showToast(message);
                ((ListFragment) pagerAdapter.getItem(1)).showGroupInfo(new ArrayList<>());
            } else {
                ((ListFragment) pagerAdapter.getItem(1)).showGroupInfo(list);
            }
        } else {
            if (dialog == null || !dialog.isShowing() && viewPager.getCurrentItem() != 2) {
                showDialogNoLeasson();
            }
        }
    }

    public void goToManualMode() {
        tabLayout.getTabAt(2).select();
        ((SettingsMainFragment) pagerAdapter.getItem(2)).showManualModeDialog();
        ((ScannerFragmentGoogleVisionFragment) pagerAdapter.getItem(0)).showManualModeDialog();
    }

    @Override
    public void showTeacherInfo(TeacherInfoResponce teacher) {
        if (teacher == null) {
            LoginActivity.start(App.getInstance(), true);
        } else {
            ((SettingsMainFragment) pagerAdapter.getItem(2)).setTeacher(teacher);
        }
    }

    @Override
    public void showSheduleContext(SheduleContextResponce teacher) {
        shedulerContext = teacher;
    }

    @Override
    public void onBackPressed() {
        if (pagerAdapter.getItem(viewPager.getCurrentItem()).getChildFragmentManager().getBackStackEntryCount() > 0) {
            pagerAdapter.getItem(viewPager.getCurrentItem()).getChildFragmentManager().popBackStack();
            if (pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof SettingsMainFragment) {
                ((SettingsMainFragment) pagerAdapter.getItem(viewPager.getCurrentItem())).hideContainer();
            }
        } else {
            super.onBackPressed();
        }
    }

    public UserInGroup getUserFromList(int id) {
        if (id == 0) {
            Toast.makeText(this, "User id is empty", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (groupList == null) {
            return null;
        }
        for (UserInGroup user : groupList) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public void saveSchedule(String name, String lessonId, String lessonTypeId, String studentGroupId, String locationId, String dateTimeStart, String duration) {
        mLoginPresenter.saveSchedule(name, lessonId, lessonTypeId, studentGroupId, locationId, dateTimeStart, duration);
    }

    @Override
    public void createSchedule() {
        if (pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof SettingsMainFragment) {
            ((SettingsMainFragment) pagerAdapter.getItem(viewPager.getCurrentItem())).hideContainer();
        }
        tabLayout.getTabAt(1).select();
        reloadInfo();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.location = location;
            Log.d("Location", "lat: " + location.getLatitude() + " lng: " + location.getLongitude() + " provider: " + location.getProvider());
        } else {
            Log.d("Location", "Location is null");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void showToast(String message) {
        super.showToast(message);
    }

    @Override
    protected void onStop() {
        if(dialog!=null){
            dialog.dismiss();
        }
        super.onStop();
    }
}
