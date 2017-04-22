package com.politipoint.android.main;

import com.politipoint.android.models.Member;

import java.util.List;

public interface MainView {

    void showProgress();

    void hideProgress();

    void setItems(List<Member> items);

    void showMessage(String message);
}
