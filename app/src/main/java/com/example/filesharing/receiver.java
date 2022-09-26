package com.example.filesharing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import okio.ByteString;

public class receiver {
    public static int part = -1;
    public static String FileName=null;
    int port = 1234;
    void receiverServer( Context Contex) throws IOException {
       Toast.makeText(Contex, "contex", Toast.LENGTH_SHORT).show();
        File dir=new File("/storage/emulated/0/Download/filesharing/sp/");

        ArrayList<Thread> thread=new ArrayList<>();
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    ServerSocket sc = new ServerSocket(port);

                    Socket s = sc.accept();
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    part = dis.readInt();
                    dis.close();
                    s.close();

                    Log.d("my", "run: "+part);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                for (int i=0;i<part;i++){
                    thread.add(null);
                }


                for (int i = 0; i < part; i++) {
                    port = port + 1;
                    int finalPort = port;
                    int finalI = i;

                    thread.set(finalI,new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ServerSocket sc1 = new ServerSocket(finalPort);
                                Socket s1 = sc1.accept();
                                System.out.println(s1.isConnected());
                                DataInputStream dis1 = new DataInputStream(s1.getInputStream());

                                String pfile="part"+finalI+".txt";

                                FileOutputStream fs=new FileOutputStream(dir.getAbsolutePath()+File.separator+pfile);

                               int i;
                                while((i=dis1.read())!=-1) {
                                    fs.write(i);
                                }


                                fs.flush();
                                fs.close();
                                dis1.close();
                                s1.close();
                                Toast.makeText(Contex, "part : "+finalI, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }));

                    thread.get(finalI).start();

                }

            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0;i<part;i++){
            try {
                thread.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
      //  Toast.makeText(Contex, "successfully getting all part", Toast.LENGTH_SHORT).show();

       // ReceivePageCont.j();
        joining j=new joining();
       File joined= j.joinFiles(Contex);
       Toast.makeText(Contex, "Joining successful", Toast.LENGTH_SHORT).show();

        if(joining.compressed){
            decompress Decompress=new decompress();
            Decompress.decompressed(joined,Contex);

        }

        else {

            File file = new File("/storage/emulated/0/Download/filesharing/final");
            File finalOutputFile=new File(file.getAbsolutePath()+File.separator+joined.getName());

            FileInputStream fileInputStream=new FileInputStream(joined);

            FileOutputStream fileOutputStream=new FileOutputStream(finalOutputFile);
            int i;
            while ((i=fileInputStream.read())!=-1) {
                fileOutputStream.write(i);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(finalOutputFile));
            Contex.sendBroadcast(intent);

        }



    }
}
