package No2.BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeServerHander implements Runnable {
    private Socket socket;
    public TimeServerHander(Socket socket) {
        this.socket=socket;
    }

    public void run() {
        BufferedReader in=null;
        PrintWriter out=null;;
        try{
            in=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out=new PrintWriter(this.socket.getOutputStream(),true);
            String cuurentTime=null;
            String body=null;
            while (true){
                body=in.readLine();
                if(body==null){
                    break;
                }
                System.out.println("The time server receive order:"+ body);
                cuurentTime="Query time:"+System.currentTimeMillis();
                System.out.println(cuurentTime);
                out.println(cuurentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            if(this.socket!=null){
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socket=null;
            }
        }
    }
}
