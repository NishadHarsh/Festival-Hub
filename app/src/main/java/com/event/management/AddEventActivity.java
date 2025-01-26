package com.event.management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    Button selectIv, uploadIv;
    ImageView iv;
    int REQUEST_CODE = 100;
    String sSelectedPath;
    EditText name;

    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestStoragePermission();
        name = findViewById(R.id.add_event_title);
        selectIv = findViewById(R.id.add_event_select_image);
        uploadIv = findViewById(R.id.add_event_upload_image);
        iv = findViewById(R.id.add_event_imageView);

        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(AddEventActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(250)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .showPreview(true)
                        .theme(R.style.Matisse_Zhihu)
                        .forResult(REQUEST_CODE);
            }
        });

        uploadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equalsIgnoreCase("")) {
                    name.setError("Event Name Required");
                } else if (sSelectedPath == null || sSelectedPath == "") {
                    Toast.makeText(AddEventActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                } else {
                    onBackPressed();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            List<Uri> selectedImage = Matisse.obtainResult(data);
            iv.setImageURI(selectedImage.get(0));
            sSelectedPath = getImage(selectedImage.get(0));
            Log.d("IMAGE_PATH", sSelectedPath);
        }
    }

    private String getImage(Uri uri) {
        if (uri != null) {
            String path = null;
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, s_array, null, null, null);
            int id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                do {
                    path = cursor.getString(id);
                }
                while (cursor.moveToNext());
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }

}