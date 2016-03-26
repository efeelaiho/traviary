package cs371m.traviary;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by efosaelaiho on 3/25/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {


        switch (position) {

            case 1:
                UnitedStates unitedStates_frag = new UnitedStates();
                return unitedStates_frag;
            case 2:
                World world_frag = new World();
                return world_frag;
            case 3:
                Location location_frag = new Location();
                return location_frag;
            case 4:
                Challenges challenges_frag = new Challenges();
                return challenges_frag;
            case 5:
                Settings settings_frag = new Settings();
                return settings_frag;
            default:
                Home home_frag = new Home();
                return home_frag;
            
        }




    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
