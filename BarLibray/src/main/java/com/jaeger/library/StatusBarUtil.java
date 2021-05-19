package com.jaeger.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.drawerlayout.widget.DrawerLayout;

public class StatusBarUtil {

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    /**
     * Set the status bar color
     *
     * @param activity Need to be set activity
     * @param color    Status bar color value
     */
    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * Set the status bar color
     *
     * @param activity       Need to be set activity
     * @param color          Status bar color value
     * @param statusBarAlpha Status bar transparency
     */

    public static void setColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                StatusBarView statusView = createStatusBarView(activity, color, statusBarAlpha);
                decorView.addView(statusView);
            }
            setRootView(activity);
        }
    }

    /**
     * Set the status bar color for the sliding back interface
     *
     * @param activity Need to be set activity
     * @param color    Status bar color value
     */
    public static void setColorForSwipeBack(Activity activity, int color) {
        setColorForSwipeBack(activity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * Set the status bar color for the sliding back interface
     *
     * @param activity       Need to be set activity
     * @param color          Status bar color value
     * @param statusBarAlpha Status bar transparency
     */
    public static void setColorForSwipeBack(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup contentView = ((ViewGroup) activity.findViewById(android.R.id.content));
            contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);
            contentView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            setTransparentForWindow(activity);
        }
    }

    /**
     * Set the solid color of the status bar No translucent effect
     *
     * @param activity Need to be set activity
     * @param color    Status bar color value
     */
    public static void setColorNoTranslucent(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    /**
     * Set the status bar color(5.0 The following has no translucent effect,Not recommended for use)
     *
     * @param activity Need to be set activity
     * @param color    Status bar color value
     */
    @Deprecated
    public static void setColorDiff(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        // Remove the translucent rectangle,So as not to overlap
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(color);
        } else {
            contentView.addView(createStatusBarView(activity, color));
        }
        setRootView(activity);
    }

    /**
     * Make the status bar semi-transparent
     *
     * Suitable for interface with picture as background,At this time, the picture needs to be filled into the status bar
     *
     * @param activity Need to be set activity
     */
    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * Make the status bar semi-transparent
     *
     * Suitable for interface with picture as background,At this time, the picture needs to be filled into the status bar
     *
     * @param activity       Need to be set activity
     * @param statusBarAlpha Status bar transparency
     */
    public static void setTranslucent(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparent(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * For the root layout is CoordinatorLayout, Make the status bar semi-transparent
     *
     * Suitable for interface with picture as background,At this time, the picture needs to be filled into the status bar
     *
     * @param activity       Need to be set activity
     * @param statusBarAlpha Status bar transparency
     */
    public static void setTranslucentForCoordinatorLayout(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * Set the status bar to be fully transparent
     *
     * @param activity Need to be set activity
     */
    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }

    /**
     * Make the status bar transparent(5.0 Above translucent effect,Not recommended for use)
     *
     * Suitable for interface with picture as background,At this time, the picture needs to be filled into the status bar
     *
     * @param activity Need to be set activity
     */
    @Deprecated
    public static void setTranslucentDiff(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Set the status bar to be transparent
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setRootView(activity);
        }
    }

    /**
     * for DrawerLayout The layout setting status bar changes color
     *
     * @param activity     Need to be set activity
     * @param drawerLayout DrawerLayout
     * @param color        Status bar color value
     */
    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * For DrawerLayout Layout setting status bar color,Solid color
     *
     * @param activity     Need to be set activity
     * @param drawerLayout DrawerLayout
     * @param color        Status bar color value
     */
    public static void setColorNoTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    /**
     * For DrawerLayout The layout setting status bar changes color
     *
     * @param activity       Need to be set activity
     * @param drawerLayout   DrawerLayout
     * @param color          Status bar color value
     * @param statusBarAlpha Status bar transparency
     */
    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color,
        int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // Generate a rectangle the size of the status bar
        // Add to statusBarView Into the layout
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
            contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
        } else {
            StatusBarView statusBarView = createStatusBarView(activity, color);
            contentLayout.addView(statusBarView, 0);
        }
        // The content layout is not LinearLayout Time,Set up padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                    contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        // Set properties
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);

        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * For DrawerLayout The layout setting status bar changes color(5.0 The following has no translucent effect
     * ,Not recommended for use)
     *
     * @param activity     Need to be set activity
     * @param drawerLayout DrawerLayout
     * @param color        Status bar color value
     */
    @Deprecated
    public static void setColorForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Generate a rectangle the size of the status bar
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, DEFAULT_STATUS_BAR_ALPHA));
            } else {
                // Add to statusBarView Into the layout
                StatusBarView statusBarView = createStatusBarView(activity, color);
                contentLayout.addView(statusBarView, 0);
            }
            // The content layout is not LinearLayout Time,Set up padding top
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            // Set properties
            ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);
        }
    }

    /**
     * For DrawerLayout Layout settings status bar transparent
     *
     * @param activity     Need to be set activity
     * @param drawerLayout DrawerLayout
     */
    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity, drawerLayout, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * For DrawerLayout Layout settings status bar transparent
     *
     * @param activity     Need to be set activity
     * @param drawerLayout DrawerLayout
     */
    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForDrawerLayout(activity, drawerLayout);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * For DrawerLayout Layout settings status bar transparent
     *
     * @param activity     Need to be set activity
     * @param drawerLayout DrawerLayout
     */
    public static void setTransparentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        // The content layout is not LinearLayout Time,Set up padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
        }

        // Set properties
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * For DrawerLayout Layout settings status bar transparent(5.0 Above translucent effect,Not recommended for use)
     *
     * @param activity     Need to be set activity
     * @param drawerLayout DrawerLayout
     */
    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Set the status bar to be transparent
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Set content layout properties
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            // Set drawer layout properties
            ViewGroup vg = (ViewGroup) drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            // Set up DrawerLayout Attributes
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    /**
     * For the head is ImageView The interface settings status bar is fully transparent
     *
     * @param activity       Need to be set activity
     * @param needOffsetView Need to be offset down View
     */
    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    /**
     * For the head is ImageView The interface settings status bar is transparent(Use default transparency)
     *
     * @param activity       Need to be set activity
     * @param needOffsetView Need to be offset down View
     */
    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    /**
     * For the head is ImageView The interface settings status bar is transparent
     *
     * @param activity       Need to be set activity
     * @param statusBarAlpha Status bar transparency
     * @param needOffsetView Need to be offset down View
     */
    public static void setTranslucentForImageView(Activity activity, int statusBarAlpha, View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForWindow(activity);
        addTranslucentView(activity, statusBarAlpha);
        if (needOffsetView != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
        }
    }

    /**
     * For fragment Head is ImageView Settings status bar is transparent
     *
     * @param activity       fragment corresponding activity
     * @param needOffsetView Need to be offset down View
     */
    public static void setTranslucentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    /**
     * For fragment Head is ImageView Settings status bar is transparent
     *
     * @param activity       fragment corresponding activity
     * @param needOffsetView Need to be offset down View
     */
    public static void setTransparentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    /**
     * For fragment Head is ImageView Settings status bar is transparent
     *
     * @param activity       fragment corresponding activity
     * @param statusBarAlpha Status bar transparency
     * @param needOffsetView Need to be offset down View
     */
    public static void setTranslucentForImageViewInFragment(Activity activity, int statusBarAlpha, View needOffsetView) {
        setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(activity);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.removeViewAt(count - 1);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * Add semi-transparent rectangular strips
     *
     * @param activity       Need to be set activity
     * @param statusBarAlpha Transparency value
     */
    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    /**
     * Generate a colored rectangular bar the same size as the status bar
     *
     * @param activity Need to be set activity
     * @param color    Status bar color value
     * @return Status bar rectangle
     */
    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color) {
        // Draw a rectangle the same height as the status bar
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    /**
     * Generate a semi-transparent rectangular bar with the same size as the status bar
     *
     * @param activity Need to be set activity
     * @param color    Status bar color value
     * @param alpha    Transparency value
     * @return Status bar rectangle
     */
    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        // Draw a rectangle the same height as the status bar
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    /**
     * Set root layout parameters
     */
    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

    /**
     * Set transparency
     */
    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Make the status bar transparent
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Create a semi-transparent rectangle View
     *
     * @param alpha Transparency value
     * @return translucent View
     */
    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        // Draw a rectangle the same height as the status bar
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    /**
     * Get the height of the status bar
     *
     * @param context context
     * @return Status bar height
     */
    private static int getStatusBarHeight(Context context) {
        // Get the height of the status bar
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * Calculate status bar color
     *
     * @param color color value
     * @param alpha alpha value
     * @return The final status bar color
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}
