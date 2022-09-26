package com.example.filesharing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class decompress {

    File decompressed(File cFile, Context c) throws IOException {

        File file = new File("/storage/emulated/0/Download/filesharing/final");

        String fileN=joining.fileName;
        fileN=fileN.substring(0,fileN.length()-4);

        File dFile = new File(file.getAbsolutePath()+File.separator+fileN);

        FileInputStream fInput = new FileInputStream(cFile.getAbsolutePath());
        FileOutputStream fOutput = new FileOutputStream(dFile.getAbsolutePath());
        long fileSize = (cFile.length());
        byte[] allBytes = new byte[(int) fileSize];
        List<Integer> compressed = new ArrayList();
        fInput.read(allBytes);

        if ((char) allBytes[0] == 'L' && (char) allBytes[1] == 'Z' && (char) allBytes[2] == 'W') {
            long x = (long) (allBytes.length - 3);
            StringBuffer codeString = new StringBuffer();

            for (int j = 3; j < allBytes.length; ++j) {
                codeString.append(String.format("%8s", Integer.toBinaryString(allBytes[j] & 255)).replace(' ', '0'));
            }

            int i;
            for (i = 1; i <= allBytes[allBytes.length - 1] + 8; ++i) {
                codeString.deleteCharAt(codeString.length() - 1);
            }

            x = (long) codeString.length();
            StringBuffer s = new StringBuffer();
            int g = 0;
            i = 0;

            while (i < codeString.length()) {
                ++g;

                if (codeString.charAt(i) == '1') {
                    if (codeString.charAt(i + 9) == '1') {
                        s.append(codeString.substring(i + 1, i + 9));
                        s.append(codeString.substring(i + 10, i + 18));
                        s.append(codeString.substring(i + 19, i + 27));
                        compressed.add(Integer.parseInt(s.substring(0, 24), 2));
                        s.delete(0, 24);
                        i += 27;
                    } else {
                        s.append(codeString.substring(i + 1, i + 9));
                        s.append(codeString.substring(i + 10, i + 18));
                        compressed.add(Integer.parseInt(s.substring(0, 16), 2));
                        s.delete(0, 16);
                        i += 18;
                    }
                } else {
                    compressed.add(Integer.parseInt(codeString.substring(i + 1, i + 9), 2));
                    i += 9;
                }
            }

            int dSize = 256;
            int y = 0;

            String f = "" + (char) (int) compressed.remove(0);

            StringBuilder decoded = new StringBuilder(f);
            x = (long) compressed.size();
            Map<Integer, String> dict = new HashMap();

            for (i = 0; i < 256; ++i) {
                dict.put(i, "" + (char) i);
            }

            String entry;
            for (Iterator var23 = compressed.iterator();
                 var23.hasNext();
                 f = entry)
            {
                int l = (Integer) var23.next();
                ++y;

                if (dict.containsKey(l)) {
                    entry = (String) dict.get(l);
                } else {
                    if (l != dSize) {
                        throw new IllegalArgumentException("Bad Compression");
                    }

                    entry = f + f.charAt(0);
                }

                decoded.append(entry);
                dict.put(dSize++, f + entry.charAt(0));
            }

            byte[] toWrite = new byte[decoded.length()];

            for (i = 0; i < decoded.length(); ++i) {
                toWrite[i] = (byte) decoded.charAt(i);
            }

            fOutput.write(toWrite);
            fInput.close();
            fOutput.close();
            ///System.out.println("Successfully Decompressed");
//
            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(cFile));
            c.sendBroadcast(intent);
    //
            return cFile;
        } else {
            fInput.close();
            fOutput.close();
            System.out.println("file can,t decompressed");
            return cFile;
        }


    }

}
