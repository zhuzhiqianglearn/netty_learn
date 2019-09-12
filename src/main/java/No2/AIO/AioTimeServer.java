package No2.AIO;

public class AioTimeServer {
    public static void main(String[] args) {
        int port=8080;
        AsyncTimeServerHander timeServer=new AsyncTimeServerHander(port);
    }
}
