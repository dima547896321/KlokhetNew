package nl.klokhet.app.flow.webview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import nl.klokhet.app.R;
import nl.klokhet.app.flow.base.ui.blank.BaseActivity;

public class WebActivity extends BaseActivity implements WebView {
    private static final String EXTRA_TYPE = "TYPE";
    private static final String MIME_TYPE = "text/html";
    private static final String ENCODING = "UTF-8";
    private static final String HTML_TOP = "<html>\n" +
            "<title>Title</title>\n" +
            "<body>";
    private static final String HTML_BOTTOM = "</body>\n" +
            "</html>";
    @InjectPresenter
    WebViewPresenter mWebViewPresenter;
    private ImageView ivBack;

    public static void start(Context context, Type type) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra(EXTRA_TYPE, type);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_web);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Type type = (Type) getIntent().getSerializableExtra(EXTRA_TYPE);
        mWebViewPresenter.loadContent(type);
        initMainProgressBar();
    }

    @Override
    public void onLoadContent(String content) {
        android.webkit.WebView webView = findViewById(R.id.webView);
        webView.loadData(HTML_TOP + content + HTML_BOTTOM, MIME_TYPE, ENCODING);
    }

    public enum Type {
        PRIVACY_POLICY, TERMS_OF_SERVICE
    }

}
