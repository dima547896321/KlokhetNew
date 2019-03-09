package nl.klokhet.app.flow.base.ui.blank;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;

import nl.klokhet.app.flow.base.error.NotImplementedInterfaceException;
import nl.klokhet.app.flow.base.presentation.presenter.blank.BaseView;
import nl.klokhet.app.utils.FullScreenKeyboardUtil;
import nl.klokhet.app.utils.KeyboardUtils;

public class BaseFragment extends MvpAppCompatFragment implements BaseView {
    public static final String TAG = "BaseActivity";
    protected static final int VISIBLE_USERS_THRESHOLD = 10;
    protected FrameLayout progressBarContainer;
    protected FullScreenKeyboardUtil mFullScreenKeyboardUtil;
    private ProgressBar mProgressBar;
    private Snackbar mSnackbar;
    private boolean isFirstResume = false;

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHideKeyBoard(String error) {
        KeyboardUtils.hideKeyboard(getActivity());
    }

    @Override
    public void showProgress() {
        if (progressBarContainer != null && progressBarContainer.getVisibility() != View.VISIBLE)
            progressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (progressBarContainer != null && progressBarContainer.getVisibility() == View.VISIBLE)
            progressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void showAlert(int message) {

    }

    @Override
    public void showAlert(String message) {

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

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backPress() {
        getActivity().finish();
    }

    protected <T> T bindInterface(Class<T> aClass, Fragment parent, Context context) {
        if (parent != null && aClass.isInstance(parent)) {
            return aClass.cast(parent);
        } else {
            if (aClass.isInstance(context)) {
                return aClass.cast(context);
            } else {
                throw new NotImplementedInterfaceException(aClass);
            }
        }
    }
}
