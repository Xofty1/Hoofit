package com.example.hoofit.ui.infoTrail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hoofit.data.Trail;

public class InfoTrailViewModel extends ViewModel {
    private MutableLiveData<Trail> trailLiveData = new MutableLiveData<>();

    public void setTrail(Trail trail) {
        trailLiveData.setValue(trail);
    }

    public LiveData<Trail> getTrailLiveData() {
        return trailLiveData;
    }
}
