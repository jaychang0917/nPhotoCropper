package com.jaychang.demo.npc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaychang.npc.NPhotoCropper;
import com.jaychang.npp.NPhotoPicker;

public class MainActivity extends AppCompatActivity {

  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button button = (Button) findViewById(R.id.pickPhoto);
    imageView = (ImageView) findViewById(R.id.imageView);

    button.setOnClickListener(view -> pickPhotos());
  }

  private void pickPhotos() {
    NPhotoPicker.with(this)
      .toolbarColor(R.color.colorPrimary)
      .statusBarColor(R.color.colorPrimary)
      .selectedBorderColor(R.color.colorPrimary)
      .limit(6)
      .pickSinglePhoto()
      .flatMap(uri -> {
        return NPhotoCropper.with(this, uri).crop();
      })
      .subscribe(uri -> {
        Glide.with(MainActivity.this).load(uri).into(imageView);
      });
  }

}
