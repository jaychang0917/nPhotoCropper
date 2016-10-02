package com.jaychang.npc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import static com.yalantis.ucrop.UCrop.EXTRA_ERROR;
import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;

public class NPhotoCropper {

  static final String EXTRA_PHOTO_SOURCE_URI = "EXTRA_PHOTO_SOURCE_URI";
  public static final int REQUEST_PHOTO_CROP = 6001;

  private Uri photoSourceUri;

  private NPhotoCropper(Uri photoSourceUri) {
    this.photoSourceUri = photoSourceUri;
  }

  public static NPhotoCropper from(Uri photoSourceUri) {
    return new NPhotoCropper(photoSourceUri);
  }

  public void start(Activity activity) {
    activity.startActivityForResult(getIntent(activity), REQUEST_PHOTO_CROP);
  }

  public void start(Fragment fragment) {
    fragment.startActivityForResult(getIntent(fragment.getActivity()), REQUEST_PHOTO_CROP);
  }

  private Intent getIntent(Context context) {
    Intent intent = new Intent(context, CropperActivity.class);
    intent.putExtra(EXTRA_PHOTO_SOURCE_URI, photoSourceUri);
    return intent;
  }

  public static Uri getCroppedPhoto(@NonNull Intent intent) {
    return intent.getParcelableExtra(EXTRA_OUTPUT_URI);
  }

  public static Throwable getError(@NonNull Intent result) {
    return (Throwable) result.getSerializableExtra(EXTRA_ERROR);
  }
}
