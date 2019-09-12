package No2.No22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient22 {
    public static void main(String[] args) throws InterruptedException {
        int port=8080;
        Socket socket=null;
        BufferedReader in=null;
        PrintWriter out=null;
        try {
            socket=new Socket("localhost",port);
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(),true);
            out.println("time is:"+System.currentTimeMillis());
//                    String readString=in.readLine();
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
    }

