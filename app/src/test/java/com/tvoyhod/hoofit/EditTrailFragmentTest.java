package com.tvoyhod.hoofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tvoyhod.hoofit.data.Coordinate;
import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.ui.editInfo.EditTrailFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
@Config(manifest=Config.NONE)
@RunWith(AndroidJUnit4.class)
public class EditTrailFragmentTest {
    @Test
    public void isNewTrailTest() {
        EditTrailFragment fragment = new EditTrailFragment();
        fragment.setNewTrail(true);
        fragment.initBundle();
        assertEquals(fragment.isNewTrail(),true);
    }
    @Test
    public void InputValidTest() {
        EditTrailFragment fragment = new EditTrailFragment();
        Reserve reserve = new Reserve();
        fragment.setReserve(reserve);
        assertFalse(fragment.isValidInput("Artem", "New trail", "medium", "12 km", "6 h", new ArrayList<>()));
    }
    @Test
    public void InputValidTest2() {
        EditTrailFragment fragment = new EditTrailFragment();
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(12.4, 34.5));
        coordinates.add(new Coordinate(1212.4, 3394.531));
        assertTrue(fragment.isValidInput("Artem", "New trail", "medium", "12 km", "6 h", coordinates));
    }
}