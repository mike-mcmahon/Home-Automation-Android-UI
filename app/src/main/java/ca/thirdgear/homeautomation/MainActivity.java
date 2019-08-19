/**
 *	███╗   ███╗██╗██╗  ██╗███████╗    ███╗   ███╗ ██████╗███╗   ███╗ █████╗ ██╗  ██╗ ██████╗ ███╗   ██╗
 *	████╗ ████║██║██║ ██╔╝██╔════╝    ████╗ ████║██╔════╝████╗ ████║██╔══██╗██║  ██║██╔═══██╗████╗  ██║
 *	██╔████╔██║██║█████╔╝ █████╗      ██╔████╔██║██║     ██╔████╔██║███████║███████║██║   ██║██╔██╗ ██║
 *	██║╚██╔╝██║██║██╔═██╗ ██╔══╝      ██║╚██╔╝██║██║     ██║╚██╔╝██║██╔══██║██╔══██║██║   ██║██║╚██╗██║
 *	██║ ╚═╝ ██║██║██║  ██╗███████╗    ██║ ╚═╝ ██║╚██████╗██║ ╚═╝ ██║██║  ██║██║  ██║╚██████╔╝██║ ╚████║
 *	╚═╝     ╚═╝╚═╝╚═╝  ╚═╝╚══════╝    ╚═╝     ╚═╝ ╚═════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
 *
 *	██████╗ ██████╗ ██████╗  ██████╗ ███████╗ █████╗ ██████╗     ██████╗ █████╗
 *	╚════██╗██╔══██╗██╔══██╗██╔════╝ ██╔════╝██╔══██╗██╔══██╗   ██╔════╝██╔══██╗
 *	 █████╔╝██████╔╝██║  ██║██║  ███╗█████╗  ███████║██████╔╝   ██║     ███████║
 *	 ╚═══██╗██╔══██╗██║  ██║██║   ██║██╔══╝  ██╔══██║██╔══██╗   ██║     ██╔══██║
 *	██████╔╝██║  ██║██████╔╝╚██████╔╝███████╗██║  ██║██║  ██║██╗╚██████╗██║  ██║
 *	╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝ ╚═════╝╚═╝  ╚═╝
 *
 *	Copyright (C) 2019 Mike McMahon, A.Sc.T.
 *
 *	**********************************************************************
 *	*****    Android Client for Home Automation RESTful web service  *****
 *  **********************************************************************
 *
 *   Author: Mike McMahon, A.Sc.T
 *   Company: 3rdGear SAI
 *   Version: 1.0
 *   Date: May 2019
 *
 */
package ca.thirdgear.homeautomation;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.setupWithNavController;

/**
 *   Main Activity class.  Container for all Fragments and configures global navigation features.
 */
public class MainActivity extends AppCompatActivity
{
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    public MainActivity()
    {
        //do nothing
    }

    /**
     * Create main activity.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        drawerLayout = findViewById(R.id.drawer_layout);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout)
                .build();

        //set up bottom navigation (in portrait mode)
        setupBottomNavMenu(navController);

        //set up side navigation menu (in landscape mode)
        setupNavigationMenu(navController);

        //configure action bar for popping back on the stack.
        setupActionBar(navController, appBarConfiguration);
    }

    /**
     * Set up the bottom navigation menu.
     * @param navController
     */
    private void setupBottomNavMenu(NavController navController)
    {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        if(bottomNav != null)
        {
            setupWithNavController(bottomNav, navController);
        }
    }

    /**
     * In split screen mode, you can drag this view out from the left
     * This does NOT modify the actionbar.
     * @param navController
     */
    private void setupNavigationMenu(NavController navController)
    {
        NavigationView sideNavView = findViewById(R.id.nav_view);
        if(sideNavView != null)
        {
            setupWithNavController(sideNavView, navController);
        }
    }

    /**
     * This allows NavigationUI to decide what label to show in the action bar
     * By using appBarConfig, it will also determine whether to
     * show the up arrow or drawer menu icon.
     * @param navController
     * @param appBarConfig
     */
    private void setupActionBar(NavController navController, AppBarConfiguration appBarConfig)
    {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
    }

    /**
     * Allows NavigationUI to support proper up navigation or the drawer layout
     * drawer menu, depending on the situation.
     * @return boolean
     */
    @Override
    public boolean onSupportNavigateUp()
    {
        return findNavController(findViewById(R.id.my_nav_host_fragment)).navigateUp();
    }
}
