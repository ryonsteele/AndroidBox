package com.politipoint.android.main;

import com.politipoint.android.models.Member;

import java.util.List;

public interface FindItemsInteractor {

    interface OnFinishedListener {
        void onFinished(List<Member> items);
    }

    void findItems(OnFinishedListener listener);
}
