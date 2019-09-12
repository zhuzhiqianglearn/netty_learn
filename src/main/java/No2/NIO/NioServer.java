package No2.NIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NioServer {
    private  ServerSocketChannel serverSocketChannel=null;
    private Selector selector=null;

  public void start() throws IOException {
      //1.打开通道（修路）
       serverSocketChannel=ServerSocketChannel.open();
      //2.绑定端口号（给路标号）,非阻塞
      serverSocketChannel.socket().bind(new InetSocketAddress("localhost",8080));
      serverSocketChannel.configureBlocking(false);
      //3,创建多路复用器（大管家）
      selector=Selector.open();
      //4.将ServerSocketChannel 注册到Selector（让大管家来管理高速公路）
      SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      while (true){
          int num=selector.select();
          if(num==0){
              System.out.println("暂无链接");
              try {
                  TimeUnit.SECONDS.sleep(2);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              continue;
          }
          Set<SelectionKey> selectionKeys = selector.selectedKeys();
          Iterator<SelectionKey> it = selectionKeys.iterator();
          while (it.hasNext()){
              SelectionKey selectionKey=(SelectionKey)it.next();
              //处理事件
              process(selectionKey);
              it.remove();
          }
      }

  }

    private  void process(SelectionKey selectionKey) throws IOException {
       if(selectionKey.isAcceptable()){
           //有客户端链接了
           ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
           SocketChannel client = server.accept();
           //设置为非阻塞
           client.configureBlocking(false);
           //将当前channel设置成读的状态
           client.register(selector,SelectionKey.OP_READ);

       }
        if(selectionKey.isReadable()){
            //返回该SelectionKey对应的 Channel，其中有数据需要读取
            SocketChannel client = (SocketChannel)selectionKey.channel();
            //往缓冲区读数据
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            int read = client.read(buff);
            if(read>0){
                buff.flip();
                byte[] bytes=new byte[buff.remaining()];
                buff.get(bytes);
                String body=new String(bytes,"UTF-8");
                System.out.println("the client send:"+body);
                client.register(selector, SelectionKey.OP_WRITE);
            }
            buff.clear();
        }
        if(selectionKey.isWritable()){
            ByteBuffer buff = ByteBuffer.allocate(1024);
            SocketChannel client = (SocketChannel)selectionKey.channel();

//            client.write(buff.wrap("hello".getBytes()));
            buff= ByteBuffer.wrap("hello".getBytes());
            buff= ByteBuffer.wrap(" zzq".getBytes());
            client.write(buff);
            client.close();

            client.close();
        }
    }

    private void doWrite(SocketChannel client, String hello) throws IOException {
        byte[] bytes=hello.getBytes();
        ByteBuffer byteBuffer= ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        client.write(byteBuffer);
    }

    public static void main(String[] args) throws IOException {
           new NioServer().start();
    }
}
