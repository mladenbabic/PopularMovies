package com.mladenbabic.popularmovies.util;

import com.mladenbabic.popularmovies.R;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email>  on 11/3/2015.
 */
public class CommonUtil {

    private static final int LESS_RATE = 4;
    private static final int MIDDLE_RATE = 6;

    public static int getRateIcon(double value, boolean darkColor) {
        if (value < LESS_RATE) {
            return darkColor ? R.drawable.ic_star_outline_dark : R.drawable.ic_star_outline;
        } else if (value > LESS_RATE && value < MIDDLE_RATE) {
            return darkColor ? R.drawable.ic_star_half_dark : R.drawable.ic_star_half;
        } else {
            return darkColor ? R.drawable.ic_star_dark : R.drawable.ic_star;
        }
    }

}
