package com.politipoint.android.Login;

public class SplashPresenterImpl implements SplashPresenter, SplashInteractor.OnLoginFinishedListener {

    private SplashView splashView;
    private SplashInteractor splashInteractor;

    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
    }

    @Override public void validateCredentials() {
        if (splashView != null) {
            splashView.showProgress();
        }

        splashInteractor.login(this);
    }

    @Override public void onDestroy() {
        splashView = null;
    }

    @Override public void onSuccess() {
        if (splashView != null) {
            splashView.hideProgress();
            splashView.navigateToHome();
        }
    }
}
