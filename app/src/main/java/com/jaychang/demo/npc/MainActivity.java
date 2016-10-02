package com.jaychang.demo.npc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaychang.npc.NPhotoCropper;
import com.jaychang.npp.NPhotoPicker;
import com.jaychang.npp.Photo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private Uri croppedPhoto;
  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button button = (Button) findViewById(R.id.pickPhoto);
    imageView = (ImageView) findViewById(R.id.imageView);


    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        pickPhotos();
      }
    });

  }

  private void pickPhotos() {
    NPhotoPicker.create()
      .toolbarColor(R.color.colorPrimary)
      .statusBarColor(R.color.colorPrimary)
      .selectedBorderColor(R.color.colorPrimary)
      .limit(6)
      .multiMode()
      .start(this);
  }

  private void cropPhoto(Uri photo) {
//    UCrop.Options options = new UCrop.Options();
//    options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));
//    options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
//    options.setHideBottomControls(true);
////
//    UCrop.of(photo, Uri.fromFile(new File(getCacheDir(), "crop.png")))
//      .withOptions(options)
//      .start(this);
    NPhotoCropper.from(photo)
      .start(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == NPhotoPicker.REQUEST_PHOTO_PICKER && resultCode == RESULT_OK) {
      List<Photo> photos = NPhotoPicker.getPickedPhotos(data);
      for (Photo photo : photos) {
        cropPhoto(photo.getUri());
      }
    }

    if (requestCode == NPhotoCropper.REQUEST_PHOTO_CROP && resultCode == RESULT_OK) {
      Uri resultUri = NPhotoCropper.getCroppedPhoto(data);
      Glide.with(this).load(resultUri).into(imageView);
    }
  }
}
