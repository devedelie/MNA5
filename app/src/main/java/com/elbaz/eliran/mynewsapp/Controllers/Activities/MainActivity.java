package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.elbaz.eliran.mynewsapp.Adapters.PageAdapter;
import com.elbaz.eliran.mynewsapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Context mContext;
    public static String category, pageTitle;
    public static String[] categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Context for the fragments
        mContext = this;

        //1 - Configuring Toolbar
        this.configureToolbar();
        //2 - Configure ViewPager
        this.configureViewPagerAndTabs();
        //3 - Configure Drawer
        this.configureDrawerLayout();
        //4 - Configure NavigationView
        this.configureNavigationView();
    }

    /**
     * 1 - Toolbar execution
     */
    private void configureToolbar(){
        // Get the toolbar view inside the activity layout
        this.toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
    }

    /**
     * 2 - ViewPager configuration + Tab Layout
     */
    private void configureViewPagerAndTabs(){
        //Get ViewPager from layout
        ViewPager pager = findViewById(R.id.activity_main_viewpager);
        //Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(mContext, getSupportFragmentManager()));

        //Get TabLayout from layout
        TabLayout tabs= findViewById(R.id.activity_main_tabs);
        //Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        //Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 3 - Configure Drawer-menu Layout (Drawer)
     */
    private void configureDrawerLayout(){
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 4 - Configure NavigationView (Drawer)
     */
    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * Navigation menu items declaration (Drawer)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Get Array from resources
        Resources resources = getResources();
        categoryList = resources.getStringArray(R.array.categories);
        // Get the order value of the item as defined in Drawer XML
        int id = menuItem.getOrder();
        // put the relevant item from the current position on the Array into category
        category = categoryList[id];
        // Make the page title with first capital letter
        pageTitle = category.substring(0,1).toUpperCase() + category.substring(1).toLowerCase();

        Log.d(TAG, "onNavigationItemSelected: clicked item ID is" + id + " "+ category);
        // Close the Drawer when item is selected
        this.drawerLayout.closeDrawer(GravityCompat.START);
        activityInstantiator();
        return true;
    }

    private void activityInstantiator (){
        Intent drawerCategory = new Intent(this, DrawerCategoriesActivity.class);
        startActivity(drawerCategory);
    }

    /**
     * Inflate the menu with the icons (search and parameters)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    /**
     * Top-Menu: icons action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.menu_activity_main_search:
                Intent searchIntent = new Intent(this, SearchAndNotificationsActivity.class);
                searchIntent.putExtra(getString(R.string.intent_search_activity_boolean), true);
                startActivity(searchIntent);
                return true;
            case R.id.over_flow_item_1:
                Intent notificationIntent = new Intent(this, SearchAndNotificationsActivity.class);
                notificationIntent.putExtra(getString(R.string.intent_notification_activity_boolean), true);
                startActivity(notificationIntent);
                return true;
            case R.id.over_flow_item_2:
                ViewDialog showHelpDialog = new ViewDialog();
                showHelpDialog.showDialog(this, getString(R.string.help_title),getString(R.string.help_upper_text), getString(R.string.about_developer), getString(R.string.about_content));
                return true;
            case R.id.over_flow_item_3:
                ViewDialog showAboutDialog = new ViewDialog();
                showAboutDialog.showDialog(this, getString(R.string.about_title),getString(R.string.about_upper_text), getString(R.string.about_developer), getString(R.string.about_content));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ViewDialog {

        public void showDialog(Activity activity,String topTitle, String msg, String msg2, String msg3){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_view);
            // Set dialog size to 97% of the screen
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.97);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.97);
            dialog.getWindow().setLayout(width, height);

            TextView dialogTitle = dialog.findViewById(R.id.topTitle);
            dialogTitle.setText(topTitle);
            TextView text = dialog.findViewById(R.id.text_dialog);
            text.setText(msg);
            TextView text2 = dialog.findViewById(R.id.text_dialog_2);
            text2.setText(msg2);
            TextView text3 = dialog.findViewById(R.id.text_dialog_3);
            text3.setText(msg3);
            Button dialogButton = dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

}

