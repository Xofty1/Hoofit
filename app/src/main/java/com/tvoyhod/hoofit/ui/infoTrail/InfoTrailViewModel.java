package com.tvoyhod.hoofit.ui.infoTrail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.data.Trail;

/**
 * The type Info trail view model.
 */
public class InfoTrailViewModel extends ViewModel {
    private MutableLiveData<Trail> trailLiveData = new MutableLiveData<>();

    /**
     * Sets trail.
     *
     * @param trail the trail
     */
    public void setTrail(Trail trail) {
        trailLiveData.setValue(trail);
    }

    /**
     * Gets trail live data.
     *
     * @return the trail live data
     */
    public LiveData<Trail> getTrailLiveData() {
        return trailLiveData;
    }
    private MutableLiveData<Reserve> reserveLiveData = new MutableLiveData<>();

    /**
     * Gets reserve live data.
     *
     * @return the reserve live data
     */
    public LiveData<Reserve> getReserveLiveData() {
        return reserveLiveData;
    }

    /**
     * Sets reserve.
     *
     * @param reserve the reserve
     */
    public void setReserve(Reserve reserve) {
        reserveLiveData.setValue(reserve);
    }
}
