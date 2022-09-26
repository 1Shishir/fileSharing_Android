package com.example.filesharing;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class sent1 {

        static boolean connected=false;
        static String receiver;
        static boolean finish=false;

        void Client(int div, ArrayList<byte[]> ar, String fileNAME) throws IOException {


            //sent file name
            String reciverIp=select.ip;

            int port=1234;
            int finalPort1 = port;

            Thread th1=new Thread(new Runnable() {
                @Override
                public void run() {

                    //sent dividing information
                    Socket s= null;
                    try {
                        s = new Socket(reciverIp, finalPort1);
                        final DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
                        connected=s.isConnected();
                        receiver=s.getInetAddress().getHostAddress();
                        dataOutputStream.writeInt(div);
                        dataOutputStream.close();
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            th1.start();
            try {
                th1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread thread[]=new Thread[div];
            //sent chunkrd files
            for (int i=0;i<div;i++){
                port=port+1;
                int finalPort = port;

                int finalI = i;
                thread[finalI]= new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Socket s1 = new Socket(reciverIp, finalPort);
                            DataOutputStream dataOutputStream1 = new DataOutputStream(s1.getOutputStream());
                            dataOutputStream1.write(ar.get(finalI));
                            dataOutputStream1.close();
                            s1.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
                thread[finalI].start();
            }

        }
    }



