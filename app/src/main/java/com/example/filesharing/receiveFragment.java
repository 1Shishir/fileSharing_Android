package com.example.filesharing;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.InetAddresses;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link receiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class receiveFragment extends Fragment {

    TextView ip;
    TextView status;
    ImageView cancel;
    LottieAnimationView lv;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public receiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment receiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static receiveFragment newInstance(String param1, String param2) {
        receiveFragment fragment = new receiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_receive, container, false);


                ip=view.findViewById(R.id.ip);
                status=view.findViewById(R.id.stat);
                cancel=view.findViewById(R.id.cancel);
                lv=view.findViewById(R.id.lottieAnimationView);

                Context context = requireContext().getApplicationContext();
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                ip.setText(Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()));


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lv.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.INVISIBLE);

                        //
                        Dexter.withContext(getContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                                    receiver r = new receiver();
                                    //r.receiverServer(server.this);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                r.receiverServer(getContext());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                Toast.makeText(context, "Server Start", Toast.LENGTH_SHORT).show();

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
                });

                        //

//                        Intent intent=new Intent(getContext(),server.class);
//                        startActivity(intent);
//                    }
//                });


        return view;
    }
}

//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            DataInputStream dis1;
//
//                            ServerSocket ss1 = new ServerSocket(port-1);
//                            Socket s1=ss1.accept();
//                            if(s1.isConnected()){
//                                status.setText("Connected with "+s1.getInetAddress().getHostName());
//                            }
//                            dis1=new DataInputStream(s1.getInputStream());
//
//                            File file= new File(Environment.getExternalStorageDirectory()+File.separator+"File Sharing"+File.separator+dis1.readUTF());
//                            file.getParentFile().mkdirs();
//                            FileOutputStream fos=new FileOutputStream(file);
//                            fos.write(new byte[]{1,2,3,4,5,6});
//
//                            fos.close();
//
//                            dis1.close();
//                            s1.close();
//                            ss1.close();
//

////for data

//
//                    DataInputStream dis;
//
//                    ServerSocket ss = new ServerSocket(port);
//                    Socket s=ss.accept();
//
//                    dis=new DataInputStream(s.getInputStream());
//
//                    StringBuilder out=new StringBuilder();
//                    int i;
//                    while((i=dis.read())!=-1){
//                        out.append(i);
//                    }
//
//                    int a[]=new int[out.length()];
//                    byte b[]=new byte[out.length()];
//                    for(int ix=0;ix<out.length();ix++){
//                        b[ix]=(byte) (int)(out.toString().charAt(ix));
//                    }
//
//
//                    fos.write(b);
//                    fos.close();
//
//                    dis.close();
//                    s.close();
//                    ss.close();
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//     Log.d(TAG, "my1");
//  File file= new File(Environment.getExternalStorageDirectory()+File.separator+"Fs");
//                    File f=new File("/storage/emulated/0/ff");
//                    ip.setText(String.valueOf(f.mkdirs()));

//     try {

// FileOutputStream fos=new FileOutputStream(file);

//                    Log.d(TAG, "my178");
//                    fos.write(4);
//                    Log.d(TAG, "my180");
//                    fos.close();
//                    Log.d(TAG, "my182");

//    } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }




//





//
//        int port=1234;
//
//        ip=view.findViewById(R.id.ip);
//        status=view.findViewById(R.id.stat);
//
//        Context context = requireContext().getApplicationContext();
//        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//
//        ip.setText(Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()));
//

//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    DataInputStream dis1;
//
//                    ServerSocket ss1 = new ServerSocket(port-1);
//                    Socket s1=ss1.accept();
//                    if(s1.isConnected()){
//                        status.setText("Connected with "+s1.getInetAddress().getHostName());
//                    }
//                    dis1=new DataInputStream(s1.getInputStream());
//
//                  File file= new File(Environment.getExternalStorageDirectory()+File.separator+"File Sharing"+File.separator+dis1.readUTF());
//                  file.getParentFile().mkdirs();
//                  FileOutputStream fos=new FileOutputStream(file);
//                  fos.write(new byte[]{1,2,3,4,5,6});
//
//                  fos.close();
//
//                  dis1.close();
//                  s1.close();
//                  ss1.close();


////for data

//
//                    DataInputStream dis;
//
//                    ServerSocket ss = new ServerSocket(port);
//                    Socket s=ss.accept();
//
//                    dis=new DataInputStream(s.getInputStream());
//
//                    StringBuilder out=new StringBuilder();
//                    int i;
//                    while((i=dis.read())!=-1){
//                        out.append(i);
//                    }
//
//                    int a[]=new int[out.length()];
//                    byte b[]=new byte[out.length()];
//                    for(int ix=0;ix<out.length();ix++){
//                        b[ix]=(byte) (int)(out.toString().charAt(ix));
//                    }
//
//
//                    fos.write(b);
//                    fos.close();
//
//                    dis.close();
//                    s.close();
//                    ss.close();

//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();





// receive r=new receive(9500,getContext());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    DataInputStream dis;
//
//                    ServerSocket ss = new ServerSocket(1233);
//                    Socket s=ss.accept();
//                    if(s.isConnected()){
//                        status.setText("Connected with "+s.getInetAddress().getHostName());
//                    }
//                    dis=new DataInputStream(s.getInputStream());
//
//                    //    Toast.makeText(context, dis.readUTF(), Toast.LENGTH_SHORT).show();
//
//
//                    String str=dis.readUTF();
//
//                    dis.close();
//                    s.close();
//                    ss.close();
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//  Log.d(TAG, "my4");