package nl.klokhet.app.flow.webview;


import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nl.klokhet.app.data.network.providers.ProviderModule;
import nl.klokhet.app.data.network.responce.BaseResponce;
import nl.klokhet.app.data.network.responce.PrivacyPolicy;
import nl.klokhet.app.data.network.responce.Status;
import nl.klokhet.app.flow.base.presentation.presenter.blank.BasePresenter;
import nl.klokhet.app.utils.RxUtils;
import okhttp3.ResponseBody;

@InjectViewState
public class WebViewPresenter extends BasePresenter<WebView> {

    private Consumer<BaseResponce<Status, PrivacyPolicy>> result = result -> {
        getViewState().onLoadContent(result.getData().getData());
        hideProgress();
    };

    public void loadContent(WebActivity.Type type) {
        switch (type) {
            case PRIVACY_POLICY:
                showProgress();
                checkNetworkAndAddDisposable(true,
                        ProviderModule.getUserProvider()
                                .policy()
                                .compose(RxUtils.ioToMainTransformerSingle())
                                .subscribe(result, this::onError)
                );
                break;

            case TERMS_OF_SERVICE:
                showProgress();
                checkNetworkAndAddDisposable(true,
                        ProviderModule.getUserProvider()
                                .policy()
                                .compose(RxUtils.ioToMainTransformerSingle())
                                .subscribe(result, this::onError)
                );
                break;
        }
    }
}
