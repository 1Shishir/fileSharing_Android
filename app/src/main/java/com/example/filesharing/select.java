package com.example.filesharing;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class select extends AppCompatActivity {
    static String ip="192.168.0.8";
    static int port=1234;

    TextInputEditText editTextIP;
    ImageView enter;
    ImageView allFile;
    ImageView music;
    ImageView video;
    ImageView photo;
    TextView status;
    ImageView sent;
    TextView fileName;

    ImageView compr;
    EditText nth;

    Intent intent;
    File selectedFile=null;

    static boolean isCompressed=false;
    static int nTh=1;

    File compressedFile=null;
    compress compress=new compress();
    ArrayList<byte[]> splitedFiles=new ArrayList<>();
    indexing indexing=new indexing();
    int splitSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);




        editTextIP=findViewById(R.id.editText);
        enter=findViewById(R.id.enter);
        allFile=findViewById(R.id.allFiles);
        music=findViewById(R.id.music);
        video=findViewById(R.id.video);
        photo=findViewById(R.id.img);
        status=findViewById(R.id.textView);

        sent=findViewById(R.id.sent);

        fileName=findViewById(R.id.textView2);
        nth=findViewById(R.id.nth);
        compr=findViewById(R.id.compButton);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                compr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isCompressed){
                            isCompressed=false;
                        }
                        else {
                            isCompressed=true;
                        }
                    }
                });




                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editTextIP.getText()!=null){
                            ip=editTextIP.getText().toString().trim();
                            status.setText(ip);
                        }
                        else{
                            status.setText("Connect with Device");
                        }
                    }
                });

                allFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent,1);
                    }
                });


                music.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent,2);
                    }
                });


                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,3);
                    }
                });

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        startActivityForResult(intent,4);
                    }
                });

                sent.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {

                        try {

                            ////
                            nTh=Integer.parseInt(String.valueOf(nth.getText()));

                            Log.d(TAG, "my:"+nTh);

                            if (selectedFile != null) {
                                if (isCompressed) {
                                    compressedFile = compress.compressFile(selectedFile);
                                } else {
                                    compressedFile = selectedFile;
                                }
                                // split
                                Log.d(TAG, "compress");
                                splitedFiles = indexing.index(compressedFile, nTh);
                                splitSize = indexing.totalPart;
                                Log.d(TAG, "indexing");
                                sent1 s = new sent1();
                                s.Client(nTh, splitedFiles, selectedFile.getName());

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    });
            }


            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();


    }





    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

                if(resultCode==RESULT_OK){
                    //String path=data.getData().getPath();
//                    selectedFile=new File(path);
//                    fileName.setText(selectedFile.getName());

                   String path=uriToPath.getPath(getApplicationContext(),data.getData());
                   selectedFile=new File(path);
                   fileName.setText(selectedFile.getName());

                 //   Log.d(TAG,"file:"+selectedFile.getAbsolutePath());
                    //





                }



    }
}