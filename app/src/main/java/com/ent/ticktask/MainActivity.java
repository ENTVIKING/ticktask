
package com.ent.ticktask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.ent.ticktask.Reminder.NotificationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 1200;

    private final Handler splashHandler =
            new Handler(Looper.getMainLooper());

    private View barContainer;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabAdd;
    private NavController navController;

    private final ActivityResultLauncher<String> notificationPermission =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    granted -> {
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Install Android system splash
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // RTL layout
        getWindow().getDecorView()
                .setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Show custom branding splash
        setContentView(R.layout.activity_splash_branding);

        // Wait before opening main screen
        splashHandler.postDelayed(
                this::showMainScreen,
                SPLASH_DURATION
        );
    }

    private void showMainScreen() {

        // Load original main layout
        setContentView(R.layout.activity_main);

        // Initialize views
        barContainer = findViewById(R.id.barContainer);
        bottomNav = findViewById(R.id.bottomNav);
        fabAdd = findViewById(R.id.fabAdd);

        // Setup window colors
        Window window = getWindow();
        window.setStatusBarColor(
                ContextCompat.getColor(this, R.color.purple_primary)
        );

        window.setNavigationBarColor(
                ContextCompat.getColor(this, R.color.purple_primary_dark)
        );
        window.getDecorView().setSystemUiVisibility(0);

        // Initialize main components
        setupInsets(findViewById(R.id.root));
        setupNavigation();
        setupNotifications();
    }

    private void setupInsets(View root) {

        int baseMargin = (int) (
                2 * getResources().getDisplayMetrics().density
        );

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {

            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
            );

            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams)
                            barContainer.getLayoutParams();

            params.bottomMargin = baseMargin + bars.bottom;

            barContainer.setLayoutParams(params);

            return insets;
        });
    }

    private void setupNavigation() {

        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        navController = navHost.getNavController();

        bottomNav.setItemActiveIndicatorEnabled(false);

        bottomNav.getMenu()
                .findItem(R.id.placeholder)
                .setEnabled(false);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.placeholder) {
                return false;
            }

            NavDestination current =
                    navController.getCurrentDestination();

            if (current != null && current.getId() == id) {
                return true;
            }


            NavOptions options = new NavOptions.Builder()
                    // Forward animations
                    .setEnterAnim(R.anim.slide_fade_in)
                    .setExitAnim(R.anim.slide_fade_out)

                    // Back animations
                    .setPopEnterAnim(R.anim.slide_fade_pop_in)
                    .setPopExitAnim(R.anim.slide_fade_pop_out)

                    // Navigation behavior
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.bottom_nav_home, true)

                    .build();

            try {

                navController.navigate(id, null, options);
                return true;

            } catch (IllegalArgumentException e) {

                Log.e(
                        "MainActivity",
                        "No destination for menu item " + id,
                        e
                );

                return false;
            }
        });

        bottomNav.setOnItemReselectedListener(item -> {
        });


        NavOptions fabOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_fade_in)
                .setExitAnim(R.anim.slide_fade_out)
                .setPopEnterAnim(R.anim.slide_fade_pop_in)
                .setPopExitAnim(R.anim.slide_fade_pop_out)
                .setLaunchSingleTop(true)
                .build();

        fabAdd.setOnClickListener(v ->
                navController.navigate(
                        R.id.addTaskFragment,
                        null,
                        fabOptions
                )
        );


        Set<Integer> mainScreens = new HashSet<>(
                Arrays.asList(
                        R.id.bottom_nav_home,
                        R.id.bottom_nav_tasks,
                        R.id.bottom_nav_reports,
                        R.id.bottom_nav_settings
                )
        );

        navController.addOnDestinationChangedListener(
                (controller, destination, args) -> {

                    MenuItem menuItem = bottomNav.getMenu()
                            .findItem(destination.getId());

                    if (menuItem != null) {
                        menuItem.setChecked(true);
                    }

                    setBarVisible(
                            mainScreens.contains(destination.getId())
                    );

                    boolean isHome =
                            destination.getId() == R.id.bottom_nav_home;

                    WindowCompat.getInsetsController(
                            getWindow(),
                            getWindow().getDecorView()
                    ).setAppearanceLightStatusBars(!isHome);
                }
        );
    }

    private void setBarVisible(boolean visible) {

        if (visible) {

            barContainer.setVisibility(View.VISIBLE);

            barContainer.animate()
                    .translationY(0)
                    .alpha(1f)
                    .setDuration(200)
                    .start();

        } else {

            barContainer.animate()
                    .translationY(barContainer.getHeight())
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() ->
                            barContainer.setVisibility(View.GONE)
                    )
                    .start();
        }
    }

    private void setupNotifications() {

        NotificationHelper.createChannel(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {

            notificationPermission.launch(
                    Manifest.permission.POST_NOTIFICATIONS
            );
        }
    }

    @Override
    protected void onDestroy() {

        splashHandler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
