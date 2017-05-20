package com.politipoint.android.Login;

public interface SplashInteractor {

    interface OnSplashFinishedListener {
        void onSuccess();
    }

    void GoHome(OnSplashFinishedListener listener);

}
