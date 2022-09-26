package com.example.filesharing;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class indexing {

    public int totalPart=0;
    public int unit=0;


    ArrayList<byte[]> index(File file, int part) throws IOException {
        //totalPart=part;

        //define compressed or not
        byte[] comp=new byte[1];
        if(select.isCompressed){
            comp[0]=1;
        }
        else{
            comp[0]=0;
        }

        FileInputStream fis1 = new FileInputStream(file.getAbsolutePath());
        //name
        byte[] fName=file.getName().getBytes();
        String lenghtN = String.format("%03d", fName.length);
        byte[] nLength=lenghtN.getBytes();
        //data
      //  int length=(int)file.length();
        //byte[] b=fis1.readAllBytes();
       // Log.d("log1s", String.valueOf(System.currentTimeMillis()));

        byte b[]=new byte[(int) file.length()];
        fis1.read(b);

         //Log.d("log1f",String.valueOf(System.currentTimeMillis()));
        // add all
        ByteArrayOutputStream concat=new ByteArrayOutputStream();

        //
       // Log.d("log2s", String.valueOf(System.currentTimeMillis()));
        concat.write(comp);
       // Log.d("log2f", String.valueOf(System.currentTimeMillis()));
        //

        concat.write(nLength);
        concat.write(fName);

        concat.write(b);

        //add file size and file name in a sinle byte array

        byte[] allBytes=concat.toByteArray();

        //split is start
       // Log.d("log4s", String.valueOf(System.currentTimeMillis()));
        ArrayList<byte[]> spFiles=new ArrayList<byte[]>();

        int fileSize=allBytes.length;

        unit=(int) Math.ceil((double) fileSize/part);
        int first=0;
        int last=0;
        int tempPart=part-1;


        for (int i = 0; i<tempPart; i++) {
            first=last;
            last=first+unit;
            spFiles.add(Arrays.copyOfRange(allBytes,first,last));
        }
        spFiles.add(Arrays.copyOfRange(allBytes,last,fileSize));
  //      Log.d("log5s", String.valueOf(System.currentTimeMillis()));
        return spFiles;
    }

}
