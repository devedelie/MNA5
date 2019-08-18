package com.elbaz.eliran.mynewsapp.controllers.activities;

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

import com.elbaz.eliran.mynewsapp.adapters.PageAdapter;
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
    public static String[] categoryList, dialogTextArray;

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
        //3 - Configure Drawer layout and NavigationView
        this.configureDrawerLayoutAndNavigationView();
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
        // Set the offscreenLimit - loads 3 fragments offScreen to improves fluency of visual load
        pager.setOffscreenPageLimit(3);

        //Get TabLayout from layout
        TabLayout tabs= findViewById(R.id.activity_main_tabs);
        //Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        //Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 3 - Configure Drawer-menu Layout + NavigationView (Drawer)
     */
    private void configureDrawerLayoutAndNavigationView(){
        // Configure drawer layout
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Configure NavigationView & set item selection listener
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
        // Get the order of the item as defined in Drawer XML
        int id = menuItem.getOrder();
        // Get the item with the same position on the Array
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
     * Inflate the top-menu (menu with search and parameters icons)
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
        int order = item.getOrder();
        // Invoke Activity if user press Search OR Notifications from menu (items order 0/1)
        if (order == 0 || order == 1){
            Intent searchIntent = new Intent(this, SearchAndNotificationsActivity.class);
            Resources resources = getResources();
            String[] stringArray = resources.getStringArray(R.array.intentSearchOrNotificationsActivity);
            searchIntent.putExtra(stringArray[order], true);
            startActivity(searchIntent);
        }else if (order == 2 || order == 3){
            // Invoke Dialog if user press on Help OR About from menu (items order 2/3)
            ViewDialog showRelevantDialog = new ViewDialog();
            showRelevantDialog.showDialog(this, order);
        }
        return super.onOptionsItemSelected(item);
    }

    public class ViewDialog {
        public void showDialog(Activity activity,int order){
            Resources resources = getResources();
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_view);
            // Set dialog size to 97% of the screen
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.97);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.97);
            dialog.getWindow().setLayout(width, height);

            // Load: Help OR About Array texts
            if (order == 2){
                dialogTextArray = resources.getStringArray(R.array.dialog_help_text);
            }else if (order == 3){
                dialogTextArray = resources.getStringArray(R.array.dialog_about_text);
            }
            // Set view elements (get text from Array in strings.xml)
            TextView dialogTitle = dialog.findViewById(R.id.topTitle);
            dialogTitle.setText(dialogTextArray[0]);
            TextView text = dialog.findViewById(R.id.text_dialog);
            text.setText(dialogTextArray[1]);
            TextView text2 = dialog.findViewById(R.id.text_dialog_2);
            text2.setText(dialogTextArray[2]);
            TextView text3 = dialog.findViewById(R.id.text_dialog_3);
            text3.setText(dialogTextArray[3]);
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

