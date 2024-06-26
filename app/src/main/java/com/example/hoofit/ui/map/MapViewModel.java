package com.example.hoofit.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hoofit.data.Trail;

public class MapViewModel extends ViewModel {
    private MutableLiveData<Trail> selectedTrailLiveData = new MutableLiveData<>();

    public void setSelectedTrail(Trail trail) {
        selectedTrailLiveData.setValue(trail);
    }

    public LiveData<Trail> getSelectedTrailLiveData() {
        return selectedTrailLiveData;
    }
}