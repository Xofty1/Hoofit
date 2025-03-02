package com.tvoyhod.hoofit.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tvoyhod.hoofit.data.Trail;

/**
 * The type Map view model.
 */
public class MapViewModel extends ViewModel {
    private MutableLiveData<Trail> selectedTrailLiveData = new MutableLiveData<>();

    /**
     * Sets selected trail.
     *
     * @param trail the trail
     */
    public void setSelectedTrail(Trail trail) {
        selectedTrailLiveData.setValue(trail);
    }

    /**
     * Gets selected trail live data.
     *
     * @return the selected trail live data
     */
    public LiveData<Trail> getSelectedTrailLiveData() {
        return selectedTrailLiveData;
    }
}