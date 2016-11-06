package com.jaychang.npc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.io.File;
import java.net.URI;

import rx.Observable;
import rx.subjects.PublishSubject;

import static com.yalantis.ucrop.UCrop.EXTRA_ERROR;
import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;

public class NPhotoCropper {

  static final String EXTRA_PHOTO_SOURCE_URI = "EXTRA_PHOTO_SOURCE_URI";
  private Uri photoSourceUri;
  @SuppressLint("StaticFieldLeak")
  private static NPhotoCropper instance;
  private Context appContext;
  private PublishSubject<Uri> photoEmitter;

  private NPhotoCropper(Context context, Uri photoSourceUri) {
    this.appContext = context;
    this.photoSourceUri = photoSourceUri;
  }

  public static synchronized NPhotoCropper with(Context context, Uri photoSourceUri) {
    if (instance == null) {
      instance = new NPhotoCropper(context.getApplicationContext(), photoSourceUri);
    }
    return instance;
  }

  static NPhotoCropper getInstance() {
    return instance;
  }

  public Observable<Uri> crop() {
    photoEmitter = PublishSubject.create();
    Intent intent = new Intent(appContext, CropperActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(EXTRA_PHOTO_SOURCE_URI, photoSourceUri);
    appContext.startActivity(intent);
    return photoEmitter;
  }

  void onPhotoCropped(Uri uri) {
    if (photoEmitter != null) {
      Uri copy = Uri.fromFile(new File(uri.getPath()));
      photoEmitter.onNext(copy);
      photoEmitter.onCompleted();
    }
  }

  void onPhotoCropError(Throwable throwable) {
    if (photoEmitter != null) {
      Throwable copy = new Throwable(throwable);
      photoEmitter.onError(copy);
    }
  }

  public static Uri getCroppedPhoto(@NonNull Intent intent) {
    return intent.getParcelableExtra(EXTRA_OUTPUT_URI);
  }

  public static Throwable getError(@NonNull Intent result) {
    return (Throwable) result.getSerializableExtra(EXTRA_ERROR);
  }
}
