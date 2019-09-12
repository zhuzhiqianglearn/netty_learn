package No2.BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
        new Thread(new Runnable() {
            public void run() {
//                try {
//                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                int port=8080;
                Socket socket=null;
                BufferedReader in=null;
                PrintWriter out=null;
                try {
                    socket=new Socket("localhost",port);
                    in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out=new PrintWriter(socket.getOutputStream(),true);
                    out.println("time is:"+System.currentTimeMillis());
                    String readString=in.readLine();

                    System.out.println(readString);
                }catch (Exception e){

                }finally {
                    if(in!=null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(out!=null){
                        out.close();
                        out=null;
                    }
                    if(socket!=null){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        socket=null;
                    }
                }
            }
        }).start();
    }
    }
}
