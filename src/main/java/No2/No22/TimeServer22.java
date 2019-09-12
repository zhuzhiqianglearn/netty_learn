package No2.No22;

import No2.BIO.TimeServerHander;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer22 {
    public static void main(String[] args) {
        int port=8080;
        ServerSocket server=null;
        try {
            server = new ServerSocket(port);
            System.out.println("The timeServer is starting in port:"+port);
            Socket socket=null;
            TimeServerHanderExcutePool excutePool=new TimeServerHanderExcutePool(10,20);
            while (true){
                socket=server.accept();
                excutePool.excute(new TimeServerHander(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server!=null){
                System.out.println("The timeServerHander close");
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server=null;
            }
        }
    }
}
