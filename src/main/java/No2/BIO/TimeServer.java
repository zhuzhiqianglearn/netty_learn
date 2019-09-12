package No2.BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
    public static void main(String[] args) {
        int port=8080;
        ServerSocket server=null;
        try {
            server = new ServerSocket(port);
            System.out.println("The timeServer is starting in port:"+port);
            Socket socket=null;
            while (true){
                socket=server.accept();
                new Thread(new TimeServerHander(socket)).start();
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
