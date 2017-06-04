package com.politipoint.android.main;

import com.politipoint.android.models.Member;

import java.util.List;

public interface FilterItemsInteractor {

    interface OnFinishedListener {
        void onFinished(List<Member> items);
    }

    void filterItems(OnFinishedListener listener, String val);
}
