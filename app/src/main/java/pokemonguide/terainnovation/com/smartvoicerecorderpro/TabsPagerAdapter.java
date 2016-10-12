package pokemonguide.terainnovation.com.smartvoicerecorderpro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new MainFragment();

            case 1:
                return new FileFragment();
        }

        return  new MainFragment();
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}