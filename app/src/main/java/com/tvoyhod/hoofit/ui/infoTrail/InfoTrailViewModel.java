package com.tvoyhod.hoofit.ui.infoTrail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.data.Trail;

public class InfoTrailViewModel extends ViewModel {
    private MutableLiveData<Trail> trailLiveData = new MutableLiveData<>();

    public void setTrail(Trail trail) {
        trailLiveData.setValue(trail);
    }

    public LiveData<Trail> getTrailLiveData() {
        return trailLiveData;
    }
    private MutableLiveData<Reserve> reserveLiveData = new MutableLiveData<>();

    public LiveData<Reserve> getReserveLiveData() {
        return reserveLiveData;
    }

    public void setReserve(Reserve reserve) {
        reserveLiveData.setValue(reserve);
    }
}
