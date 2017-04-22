package com.politipoint.android.Login;

public interface SplashInteractor {

    interface OnLoginFinishedListener {
        void onSuccess();
    }

    void login(OnLoginFinishedListener listener);

}
