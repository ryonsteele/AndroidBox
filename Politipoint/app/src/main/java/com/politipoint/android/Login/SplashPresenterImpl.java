package com.politipoint.android.Login;

public class SplashPresenterImpl implements SplashPresenter, SplashInteractor.OnSplashFinishedListener {

    private SplashView splashView;
    private SplashInteractor splashInteractor;

    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
    }

    @Override public void validate() {
        if (splashView != null) {
            splashView.showProgress();
        }

        splashInteractor.GoHome(this);
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
