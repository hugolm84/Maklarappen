/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Drawer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.itm.projekt.Activities.SettingsPreferenceActivity;
import com.itm.projekt.Adapters.SectionedNotificationAdapter;
import com.itm.projekt.Items.NotificationItem;
import com.itm.projekt.MainActivity;
import com.itm.projekt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class DrawerMenu {


    private static final String SETTINGS_FRAGMENT = "SettingsFragment";
    private static final String TAG = DrawerMenu.class.getName();

    private SectionedNotificationAdapter mDrawListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Activity context;

    public static interface DrawerMenuListener {
        void onDrawerClosed(View view);
        void onDrawerOpened(View view);
    }

    public DrawerMenu(final Activity context) {

        this.context = context;

        mDrawListAdapter = new SectionedNotificationAdapter(context, R.layout.drawer_list_menu_item,  R.layout.drawer_list_header, new ArrayList<NotificationItem>());

        addMenuItems("base_menu");
        addMenuItems("search_menu");

        mDrawerLayout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList = (ListView) context.findViewById(R.id.left_drawer_base);
        mDrawerList.setAdapter(mDrawListAdapter);

        mDrawerList.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos,long id){

                // Its a section header, do nothing.
                if( mDrawListAdapter.getItem(pos).getFragmentId().isEmpty())
                    return;

                mDrawerLayout.setDrawerListener( new DrawerLayout.SimpleDrawerListener(){
                    @Override
                    public void onDrawerClosed(View drawerView){
                        super.onDrawerClosed(drawerView);

                        NotificationItem item =  mDrawListAdapter.getItem(pos);

                        /**
                         * This is really annoying! GoogleMaps requires v4support.
                         * We cannot cast PreferenceFragment to a v4.Fragment. So instead, we start a new activity
                         */
                        if(item.getFragmentId().contains(SETTINGS_FRAGMENT)) {
                            Intent intent = new Intent();
                            intent.setClass(context, SettingsPreferenceActivity.class);
                            context.startActivityForResult(intent, 0);
                        }
                        else {

                            String fragmentId = item.getFragmentId();
                            Fragment fragment = ((MainActivity)context).getFragment(fragmentId);

                            if(fragment == null) {
                                //Toast.makeText(context, "Kunde inte hitta " + fragmentId, Toast.LENGTH_SHORT).show();
                                fragment = Fragment.instantiate(context, fragmentId);
                            }

                            // We add instead of replace, this way we can save mapStates etc
                            Fragment currentFragment = ((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.content_frame);
                            FragmentTransaction tx = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

                            if(currentFragment != null)
                                tx.hide(currentFragment);

                            if(fragment.isAdded()) {
                                tx.show(fragment);
                            } else {

                                tx.add(R.id.content_frame, fragment).addToBackStack(fragmentId);
                            }

                            tx.commit();
                        }
                    }
                });

                closeDrawer();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(context, mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close )
        {
            public void onDrawerClosed(View view) {
                if(context instanceof DrawerMenuListener)
                     ((DrawerMenuListener) context).onDrawerClosed(view);
                context.invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                if(context instanceof DrawerMenuListener)
                    ((DrawerMenuListener) context).onDrawerOpened(view);
                context.invalidateOptionsMenu();
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void addMenuItems(String id) {

        int menu = context.getResources().getIdentifier(id, "array", context.getPackageName());
        int icon = context.getResources().getIdentifier(id+"_icons", "array", context.getPackageName());
        int fragments = context.getResources().getIdentifier(id+"_fragments", "array", context.getPackageName());

        String[] baseMenu = context.getResources().getStringArray(menu);
        String[] baseMenuFragments = context.getResources().getStringArray(fragments);
        TypedArray icons = context.getResources().obtainTypedArray(icon);

        for(int i = 0; i < baseMenu.length; i++) {
            NotificationItem item = new NotificationItem( baseMenu[i], icons.getResourceId(i, -1),  baseMenuFragments[i]);
            if(i==0) mDrawListAdapter.addSectionItem(item);
            else mDrawListAdapter.addItem(item);
        }
    }

    public void updateNotificationItem(final String fragmentId, int count) {
        for(NotificationItem item : mDrawListAdapter.getData()) {
            if(item.getFragmentId().equals(fragmentId)) {
                item.setNotificationCount(count);
                break;
            }
        }
        mDrawListAdapter.notifyDataSetChanged();
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }

    public void onConfigChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }
    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mDrawerList);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mDrawerList);
        context.invalidateOptionsMenu();
    }

    public void setTitle(CharSequence title) {
        context.getActionBar().setTitle(title);
    }

    public void selectItem(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public boolean isEmpty() {
        return mDrawListAdapter.isEmpty();
    }

}
