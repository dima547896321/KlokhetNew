package nl.klokhet.app.flow.webview;

import nl.klokhet.app.flow.base.presentation.presenter.blank.BaseView;

public interface WebView extends BaseView {
    public void onLoadContent(String content);
}
