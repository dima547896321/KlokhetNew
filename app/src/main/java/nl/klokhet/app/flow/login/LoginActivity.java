package nl.klokhet.app.flow.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.flow.base.ui.blank.BaseActivity;
import nl.klokhet.app.flow.forgote_pass.ForgotActivity;
import nl.klokhet.app.flow.main.MainActivity;
import nl.klokhet.app.flow.webview.WebActivity;
import nl.klokhet.app.utils.PrefUtil;
import nl.klokhet.app.utils.TextViewUtils;
import timber.log.Timber;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {
    private final int REQUEST_PERMISSION_CAMERA = 555;
    private final int REQUEST_PERMISSION_LOCATION = 777;
    @InjectPresenter
    LoginPresenter mLoginPresenter;
    Button btnLogin;
    EditText etEmail;
    EditText etPassword;
    TextView tvForgotPass;
    CheckBox checkBox;
    private ImageView ivLetter;
    private ImageView ivLock;

    public static void start(Context context) {
        start(context, true);
    }

    public static void start(Context context, boolean clearStack) {
        Intent intent = new Intent(context, LoginActivity.class);
        PrefUtil.getInstance().cleanData();
        if (clearStack) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initMainProgressBar();
        etEmail = findViewById(R.id.etEmail);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        tvForgotPass.setOnClickListener(this);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(true);
        checkBox.setSelected(true);
        ivLetter = findViewById(R.id.ivLetter);
        ivLock = findViewById(R.id.ivLock);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeButtonColorState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.viewEmail).setBackgroundColor(App.getInstance().getResources().getColor(R.color.greenblue));
                    ivLetter.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.ic_user_new_green));
                } else {
                    findViewById(R.id.viewEmail).setBackgroundColor(App.getInstance().getResources().getColor(R.color.warmGrey));
                    ivLetter.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.ic_user_new_gray));
                }
            }
        });
        etPassword = findViewById(R.id.etPassword);
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.viewPass).setBackgroundColor(App.getInstance().getResources().getColor(R.color.greenblue));
                    ivLock.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.ic_password_new_green));
                } else {
                    findViewById(R.id.viewPass).setBackgroundColor(App.getInstance().getResources().getColor(R.color.warmGrey));
                    ivLock.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.ic_password_new_gray));
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeButtonColorState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setTypeface(Typeface.createFromAsset(getAssets(), "PT_Sans-Web-Bold.ttf"));
        btnLogin.setOnClickListener(this);
        setUpTermsAndPolicyPrivacy();
        changeButtonColorState();
        if (PrefUtil.getAutologin()) {
            Timber.d("Start autologin");
            mLoginPresenter.login(PrefUtil.getUserEmail(), PrefUtil.getUserPass(), PrefUtil.getAutologin());
        }
    }


    private void changeButtonColorState() {

    }

    @Override
    public void goToNextActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private boolean validatePassword() {
        return etPassword.getText().toString().length() >= 3;
    }

    private void setUpTermsAndPolicyPrivacy() {
        TextView tvTermsAndPolicy = findViewById(R.id.tvTermsOfService);
        TextViewUtils.makePartTextViewClickable(tvTermsAndPolicy, getString(R.string.terms_of_service),
                () -> WebActivity.start(this, WebActivity.Type.TERMS_OF_SERVICE), R.style.TermsOfUsePrivaciPolicy);
        TextViewUtils.makePartTextViewClickable(tvTermsAndPolicy, getString(R.string.privacy_policy),
                () -> WebActivity.start(this, WebActivity.Type.PRIVACY_POLICY), R.style.TermsOfUsePrivaciPolicy);
        tvTermsAndPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgotPass:
                hideKeyboard();
                startActivity(new Intent(this, ForgotActivity.class));
                break;
            case R.id.btnLogin:
                checkPermissionsCamera();
                hideKeyboard();
                break;
        }
    }

    private void allPermissionsGrunted() {
        Timber.d("Start allPermissionsGrunted");
//        mLoginPresenter.login("Illya_Vorobets_1", "111111", checkBox.isChecked());
//        mLoginPresenter.login("Illya_Vorobets_1", "6Pz9EWexuP", checkBox.isChecked());
//        mLoginPresenter.login("Sasha_Zyruk_1", "N8BkBQLV1L", checkBox.isChecked());
        mLoginPresenter.login(etEmail.getText().toString().trim(), etPassword.getText().toString(), checkBox.isChecked());
    }

    private void checkPermissionsCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                PrefUtil.setFirstRequestCamera(true);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) && !PrefUtil.getFirstRequestCamera()) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                } else {
                    openAppSettings(REQUEST_PERMISSION_CAMERA);
                }
            }
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                PrefUtil.setFirstRequestLocation(true);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && !PrefUtil.getFirstRequestLocation()) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
                } else {
                    openAppSettings(REQUEST_PERMISSION_LOCATION);
                }
            }
        } else {
            allPermissionsGrunted();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (resultCode != RESULT_OK) {
                checkPermissionsCamera();
            } else {
                Toast.makeText(this, "For use app we need permission for Camera", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (resultCode != RESULT_OK) {
                checkPermissionsCamera();
            } else {
                Toast.makeText(this, "For use app we need permission for Location", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (permissions.length == 0) {
                Toast.makeText(this, "For use app we need permission for camera", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int x : grantResults) {
                if (x != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "For use app we need permission for camera", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            checkPermissionsCamera();
        } else if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (permissions.length == 0) {
                Toast.makeText(this, "For use app we need permission for Location", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int x : grantResults) {
                if (x != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "For use app we need permission for Location", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            checkPermissionsCamera();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void openAppSettings(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, requestCode);
    }

}
