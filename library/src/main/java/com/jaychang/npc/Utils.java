package com.jaychang.npc;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;

public class Utils {

  public static void setStatusBarColor(Activity activity, @ColorRes int color) {
    if (Build.VERSION.SDK_INT >= 21) {
      if (activity.getWindow() != null) {
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
      }
    }
  }

}
