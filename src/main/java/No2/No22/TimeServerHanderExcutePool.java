package No2.No22;

import java.util.concurrent.*;

public class TimeServerHanderExcutePool {
    private ExecutorService executor;

    public TimeServerHanderExcutePool(int maxSize, int queueSize) {
//        executor=new ThreadPoolExecutor(1, 1, 2L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize), new RejectedExecutionHandler() {
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                System.out.println("chaoshile");
//            }
//        });
        executor=new ThreadPoolExecutor(1, 1, 2L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize),new ThreadPoolExecutor.DiscardOldestPolicy());
    }
    public void excute(java.lang.Runnable task){
        executor.execute(task);
    }
}
