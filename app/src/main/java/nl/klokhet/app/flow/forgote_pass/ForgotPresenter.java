package nl.klokhet.app.flow.forgote_pass;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.function.Consumer;

import nl.klokhet.app.data.network.providers.ProviderModule;
import nl.klokhet.app.flow.base.presentation.presenter.blank.BasePresenter;
import nl.klokhet.app.utils.RxUtils;

@InjectViewState
public class ForgotPresenter extends BasePresenter<ForgotView> {

    public void forgotePass(String email) {
        checkNetworkAndAddDisposable(true,
                ProviderModule.getUserProvider().forgotePass(email)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(done->{
                            hideProgress();
                            if(done.getStatus().getSuccess()){
                                getViewState().showToastWithText(done.getStatus().getMessage());
                            }else{
                                getViewState().showToast(done.getStatus().getMessage());
                            }
                        }, error->{
                            hideProgress();
                            Log.d("error" , error.getMessage());
                        }));
    }


    Consumer<String> done = done->{
        getViewState().backPress();
    };
}
