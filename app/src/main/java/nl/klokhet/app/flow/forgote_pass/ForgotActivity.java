package nl.klokhet.app.flow.forgote_pass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.flow.base.ui.blank.BaseActivity;

public class ForgotActivity extends BaseActivity implements ForgotView, View.OnClickListener {
    @InjectPresenter
    ForgotPresenter mLoginPresenter;

    Button btnLogin;
    EditText etEmail;
    ImageView back;
    ImageView ivLetter;

    public static void start(Context context) {
        start(context, true);
    }

    public static void start(Context context, boolean clearStack) {
        Intent intent = new Intent(context, ForgotActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_forgot);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initMainProgressBar();
        back = findViewById(R.id.ivBack);
        back.setOnClickListener(this);
        ivLetter = findViewById(R.id.ivLetter);
        etEmail = findViewById(R.id.etEmail);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                changeButtonColorState();
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
                    ivLetter.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.ic_mail_outline_green_24dp));
                } else {
                    findViewById(R.id.viewEmail).setBackgroundColor(App.getInstance().getResources().getColor(R.color.warmGrey));
                    ivLetter.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.ic_mail_outline_gray_24dp));
                }
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnLogin.setTypeface(Typeface.createFromAsset(getAssets(), "PT_Sans-Web-Bold.ttf"));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnLogin:
                if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
                    mLoginPresenter.forgotePass(etEmail.getText().toString());
                    hideKeyboard();
                } else {
                    Toast.makeText(this, "Please, input correct email", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void showToastWithText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (etEmail != null) {
            etEmail.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2800);
        }
    }
}
