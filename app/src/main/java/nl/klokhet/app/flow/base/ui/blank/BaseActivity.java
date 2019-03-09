package nl.klokhet.app.flow.base.ui.blank;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import nl.klokhet.app.App;
import nl.klokhet.app.BuildConfig;
import nl.klokhet.app.R;
import nl.klokhet.app.flow.base.presentation.presenter.blank.BaseView;
import nl.klokhet.app.utils.KeyboardUtils;
import nl.klokhet.app.utils.WindowUtils;

public class BaseActivity extends MvpAppCompatActivity implements BaseView {
    public static final String TAG = "BaseActivity";
    protected FrameLayout progressBarContainer;
    private Snackbar mSnackbar;
    private boolean isFirstResume = false;

    protected void initMainProgressBar() {
        progressBarContainer = findViewById(R.id.progressBarContainer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(App.getInstance().getResources().getColor(R.color.white));
        }
        initMainProgressBar();
    }


    @Override
    public void onError(String error) {
        hideKeyboard();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showProgress() {
        if (progressBarContainer != null && progressBarContainer.getVisibility() != View.VISIBLE) {
            progressBarContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        }
        if (BuildConfig.DEBUG && progressBarContainer == null) {
            Toast.makeText(this, R.string.progress_bar_container, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressBarContainer != null && progressBarContainer.getVisibility() == View.VISIBLE)
            progressBarContainer.setVisibility(View.GONE);
        if (BuildConfig.DEBUG && progressBarContainer == null) {
            Toast.makeText(this, R.string.progress_bar_container, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showAlert(int message) {

    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlert(String message, String title) {

    }

    @Override
    public void showSnackBar(int res) {

    }

    @Override
    public void showSnackBar(String text) {

    }

    @Override
    public void showSnackBar(View rootView, int res) {

    }

    @Override
    public void showSnackBar(View rootView, String text) {

    }

    protected void hideStatusBar() {
        WindowUtils.hideStatusBar(getWindow());
    }


    protected void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
    }

    protected void setWindowInFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void hideKeyboard() {
        KeyboardUtils.hideKeyboard(this);
    }

    protected void hideKeyboard(Activity activity) {
        KeyboardUtils.hideKeyboard(activity);
    }

    @Override
    public void onHideKeyBoard(String error) {
        hideKeyboard();
    }

    @Override
    public void backPress() {
        finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
