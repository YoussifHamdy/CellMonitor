package com.youssif.joe.weapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



class SectionsPagerAdapter extends FragmentPagerAdapter{

    SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new SectionsFragment();

            case 1:
                return new ChatsFragment();

            case 2:
                return new FriendsFragment();

                default:
                    return null;

        }

    }



    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "GROUPS";

            case 1:
                return "CHATS";

            case 2:
                return "FRIENDS";

            default:
                return null;
        }

    }
}
