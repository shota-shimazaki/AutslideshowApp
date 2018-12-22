package com.example.shimazakishota.autslideshowapp3;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
public class MainActivity extends AppCompatActivity {
    Button mMoveButton;
    Button mBackButton;
   Button mStartButton;
   Cursor cursor;
    Timer mTimer;
    Handler mHandler = new Handler();

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            getContentsInfo();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }
    private void getContentsInfo() {
        ContentResolver resolver = getContentResolver();
        cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        mMoveButton = (Button) findViewById(R.id.move_button);
        mBackButton = (Button) findViewById(R.id.back_button);
        mStartButton=(Button)findViewById(R.id.start_button);
        if (cursor.moveToFirst()) {
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        }


        mMoveButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (cursor.moveToNext()) {
                                                   int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                                   Long id = cursor.getLong(fieldIndex);
                                                   Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                                                   ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                                   imageView.setImageURI(imageUri);
                                               } else {
                                               }
                                           }
        });
                mStartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cursor.moveToFirst()) {
                                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                            Long id = cursor.getLong(fieldIndex);
                                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                            imageView.setImageURI(imageUri);
                                        } else {
                                        }
                                    }
                                });
                            }
                        }, 200, 200);

                    }
                });
                        mBackButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
        if (cursor.moveToPrevious()) {
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        } else {
        }
    }
});
    }
}