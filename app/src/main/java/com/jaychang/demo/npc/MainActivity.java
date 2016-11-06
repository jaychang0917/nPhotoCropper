package com.jaychang.demo.npc;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaychang.npc.NPhotoCropper;
import com.jaychang.npp.NPhotoPicker;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

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
    NPhotoPicker.with(this)
      .toolbarColor(R.color.colorPrimary)
      .statusBarColor(R.color.colorPrimary)
      .selectedBorderColor(R.color.colorPrimary)
      .limit(6)
      .pickSinglePhoto()
      .flatMap(new Func1<Uri, Observable<Uri>>() {
        @Override
        public Observable<Uri> call(Uri uri) {
          return cropPhoto(uri);
        }
      })
      .subscribe(new Action1<Uri>() {
        @Override
        public void call(Uri uri) {
          Glide.with(MainActivity.this).load(uri).into(imageView);
        }
      });
  }

  private Observable<Uri> cropPhoto(Uri source) {
    return NPhotoCropper.with(this, source)
      .crop();
  }

}
