package com.example.filesharing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class joining {
    static boolean compressed;
    static String fileName;

    File joinFiles(Context c) throws IOException {
        //get name from file
        File spDir=new File("/storage/emulated/0/Download/filesharing/sp/");

        File fp=new File(spDir+File.separator+"part0.txt");

        FileInputStream fisN=new FileInputStream(fp);

        byte[] b=new byte[(int)fp.length()];
        fisN.read(b);

        fisN.close();
// check compression

        if(b[0]==1){
            compressed=true;
        }
        else{
            compressed=false;
        }
//check file name lenght

        StringBuilder nameL=new StringBuilder();
        for(int i=1;i<4;i++){
            nameL.append((char)b[i]);
        }
        int L=Integer.parseInt(nameL.toString());

        //file name
        StringBuilder fileN=new StringBuilder();
        for(int i=4;i<4+L;i++) {
            fileN.append((char) b[i]);
        }

        int from=4+L;
        int to=b.length;

        byte fi[]= Arrays.copyOfRange(b,from,to);
        FileOutputStream fosN=new FileOutputStream(spDir+File.separator+"part0.txt");
        fosN.write(fi);
        fosN.flush();
        fosN.close();

        //get filname

        String name=fileN.toString();
        fileName=name;

        File finalFiledir=new File("/storage/emulated/0/Download/filesharing/join/");
        finalFiledir.mkdir();
        File finalFile=new File(finalFiledir+File.separator+name);

        FileOutputStream fos = new FileOutputStream(finalFile);
        int part = receiver.part;

        for (int i = 0; i < part; i++) {
            int finalI = i;
            try {

                String fileName = "part" + finalI + ".txt";
                File fc=new File(spDir+File.separator+fileName);
                //byte b1[]=new byte[(int)fc.length()];
                FileInputStream fis = new FileInputStream(fc);
               int ii;
               while ((ii=fis.read())!=-1){
                   fos.write(ii);
               }
                //fos.write(b1);
                fis.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        fos.flush();
        fos.close();

        ///////////////////////
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(finalFile));
        c.sendBroadcast(intent);
        //////////////////////

        return finalFile;

    }
}
