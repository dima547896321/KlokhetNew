package nl.klokhet.app.flow.login;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.function.Consumer;

import nl.klokhet.app.data.network.NetworkModule;
import nl.klokhet.app.data.network.providers.ProviderModule;
import nl.klokhet.app.flow.base.presentation.presenter.blank.BasePresenter;
import nl.klokhet.app.utils.PrefUtil;
import nl.klokhet.app.utils.RxUtils;

@InjectViewState
public class LoginPresenter extends BasePresenter<LoginView> {

    public void login(String email, String pass, boolean saveAutologin) {
    checkNetworkAndAddDisposable(true,
        ProviderModule.getUserProvider().login(email, pass)
                .compose(RxUtils.ioToMainTransformerSingle())
                .subscribe(done->{
                    hideProgress();
                    if(done.getStatus().getSuccess()){
                        PrefUtil.setUserToken(done.getData().getToken());
                        PrefUtil.setAutologin(saveAutologin);
                        getViewState().goToNextActivity();
                        PrefUtil.setUserEmail(email);
                        PrefUtil.setUserPass(pass);
                    }else{
                        getViewState().showToast(done.getStatus().getMessage());
                    }
                }, error->{
                    hideProgress();
                    Log.d("error" , error.getMessage());
                }));
    }

}
