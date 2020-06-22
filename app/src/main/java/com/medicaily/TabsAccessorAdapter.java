package com.medicaily;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsAccessorAdapter extends FragmentStatePagerAdapter {
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                AppointmentsFragment appointmentsFragment = new AppointmentsFragment();
                return appointmentsFragment;
            case 1:
                MedicationsFragment medicationsFragment = new MedicationsFragment();
                return medicationsFragment;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Appointment";
            case 1:
                return "Medications";
            default:
                return null;
        }
    }
}
