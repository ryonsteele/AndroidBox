package com.politipoint.android.main;

import com.politipoint.android.models.Member;

import java.util.List;

public class MainPresenterImpl implements MainPresenter, FindItemsInteractor.OnFinishedListener, FilterItemsInteractor.OnFinishedListener {

    private MainView mainView;
    private FindItemsInteractor findItemsInteractor;
    private FilterItemsInteractor filterItemsInteractor;

    public MainPresenterImpl(MainView mainView, FindItemsInteractor findItemsInteractor, FilterItemsInteractor filterItemsInteractor) {
        this.mainView = mainView;
        this.findItemsInteractor = findItemsInteractor;
        this.filterItemsInteractor = filterItemsInteractor;
    }

    @Override public void onResume() {
        if (mainView != null) {
            mainView.showProgress();
        }

        findItemsInteractor.findItems(this);
    }

    @Override public void onItemClicked(int position) {
        if (mainView != null) {
            mainView.showMessage(String.format("Position %d clicked", position + 1));
        }
    }

    @Override public void onDestroy() {
        mainView = null;
    }

    @Override public void onFinished(List<Member> items) {
        if (mainView != null) {
            mainView.setItems(items);
            mainView.hideProgress();
        }
    }

    @Override public void onFilter(String val){
      filterItemsInteractor.filterItems(this, val);
    }

    public MainView getMainView() {
        return mainView;
    }
}
