package com.jaychang.npc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {

  private GestureCropImageView cropImageView;
  private OverlayView overlayView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.npc_activity_cropper);
    init();
  }

  private void init() {
    Utils.setStatusBarColor(this, android.R.color.black);
    initToolbar();
    initCropOptionsPanel();
    initCropper();
  }

  private void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);
    toolbar.setNavigationIcon(R.drawable.ic_close);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void initCropper() {
    final UCropView cropView = (UCropView) findViewById(R.id.cropView);
    cropImageView = cropView.getCropImageView();
    overlayView = cropView.getOverlayView();

    Uri photoSourceUri = getIntent().getParcelableExtra(NPhotoCropper.EXTRA_PHOTO_SOURCE_URI);
    File outputFile = new File(getCacheDir(), "nPhotoCrop_" + UUID.randomUUID().toString() + ".jpg");
    try {
      cropImageView.setImageUri(photoSourceUri, Uri.fromFile(outputFile));
    } catch (Exception e) {
      e.printStackTrace();
    }

    overlayView.setFreestyleCropEnabled(true);
    cropImageView.setRotateEnabled(true);
    cropImageView.setScaleEnabled(true);
  }

  private void initCropOptionsPanel() {
    final TextView originalOptionView = (TextView) findViewById(R.id.originalOptionView);
    final TextView wideOptionView = (TextView) findViewById(R.id.wideOptionView);
    final TextView squareOptionView = (TextView) findViewById(R.id.squareOptionView);
    final TextView rotateOptionView = (TextView) findViewById(R.id.rotateOptionView);

    originalOptionView.setSelected(true);

    originalOptionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        originalOptionView.setSelected(true);
        wideOptionView.setSelected(false);
        squareOptionView.setSelected(false);
        changeToOriginalCropOption();
      }
    });
    wideOptionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        originalOptionView.setSelected(false);
        wideOptionView.setSelected(true);
        squareOptionView.setSelected(false);
        changeToWideCropOption();
      }
    });
    squareOptionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        originalOptionView.setSelected(false);
        wideOptionView.setSelected(false);
        squareOptionView.setSelected(true);
        changeToSquareCropOption();
      }
    });
    rotateOptionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        rotatePhoto(-90);
      }
    });
  }

  private void changeToOriginalCropOption() {
    cropImageView.setTargetAspectRatio(0.0f);
  }

  private void changeToWideCropOption() {
    cropImageView.setTargetAspectRatio(3.0f / 2);
  }

  private void changeToSquareCropOption() {
    cropImageView.setTargetAspectRatio(1.0f);
  }

  private void rotatePhoto(int angle) {
    cropImageView.postRotate(angle);
    cropImageView.setImageToWrapCropBounds();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.npc_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.done) {
      cropAndSaveImage();
    }
    return true;
  }

  private void cropAndSaveImage() {
    cropImageView.cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, new BitmapCropCallback() {

      @Override
      public void onBitmapCropped(@NonNull Uri resultUri, int imageWidth, int imageHeight) {
        setResultUri(resultUri, cropImageView.getTargetAspectRatio(), imageWidth, imageHeight);
        finish();
      }

      @Override
      public void onCropFailure(@NonNull Throwable t) {
        setResultError(t);
        finish();
      }
    });
  }

  private void setResultUri(Uri uri, float resultAspectRatio, int imageWidth, int imageHeight) {
    setResult(RESULT_OK, new Intent()
      .putExtra(UCrop.EXTRA_OUTPUT_URI, uri)
      .putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio)
      .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth)
      .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight)
    );
  }

  private void setResultError(Throwable throwable) {
    setResult(UCrop.RESULT_ERROR, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
  }
}
