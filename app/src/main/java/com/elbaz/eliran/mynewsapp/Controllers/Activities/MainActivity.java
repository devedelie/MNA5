package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Context mContext;

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
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
    }

    /**
     * 2 - ViewPager configuration + Tab Layout
     */
    private void configureViewPagerAndTabs(){
        //Get ViewPager from layout
        ViewPager pager = (ViewPager)findViewById(R.id.activity_main_viewpager);
        //Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(mContext, getSupportFragmentManager()));

        //Get TabLayout from layout
        TabLayout tabs= (TabLayout)findViewById(R.id.activity_main_tabs);
        //Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        //Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 3 - Configure Drawer-menu Layout (Drawer)
     */
    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 4 - Configure NavigationView (Drawer)
     */
    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * Navigation menu items declaration (Drawer)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // 4 - Handle Navigation Item Click
        int id = menuItem.getItemId();

        switch (id){
            case R.id.activity_main_drawer_news :
                break;
            case R.id.activity_main_drawer_1:
                Toast.makeText(this, "Top Stories", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_2:
                Toast.makeText(this, "News", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        // Close the Drawer when item is selected
        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
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
                Toast.makeText(this, "action 2", Toast.LENGTH_LONG).show();
                return true;
            case R.id.over_flow_item_3:
                Toast.makeText(this, "action 3", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

