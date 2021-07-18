package com.qytx;

import java.util.concurrent.*;

/**
 * 在main函数启动一个新线程或线程池，异步运行一个方法，拿到这个方法的返回值。
 * 1、使用FutureTask
 */
public class ThreadTest {

    private static int threadResult = 0;

    private static ThreadLocal<Integer> local = new ThreadLocal<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池

        //1、使用全局变量
        //int result =byNormalOne();

        //2、使用数组接收结果
        //int result =byNormalTwo();

        //3、使用FutureTask
        //int result = byFutureTask();

        //4、使用线程池
        int result = byExecutorService();


        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }


    /**
     * 1、使用全局变量
     * @return
     */
    public static int byNormalOne() {
        new Runnable() {
            @Override
            public void run() {
                threadResult = sum();//这是得到的返回值
            }
        }.run();
        return threadResult;
    }

    /**
     * 2、使用数组接收结果
     * @return
     */
    public static int byNormalTwo() {
        int[] result = {0};
        new Runnable() {
            @Override
            public void run() {
                result[0] = sum();//这是得到的返回值
            }
        }.run();
        return result[0];
    }


    /**
     * 3、通过FutureTask获取
     * @return
     */
    public static int byFutureTask() {
        int result = 0;
        try {
            FutureTask ft = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    int result = sum();//这是得到的返回值
                    return result;
                }
            });
            // 异步执行 下面方法
            Thread thread = new Thread(ft);
            thread.start();
            result = (int) ft.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 4、通过线程池
     * @return
     */
    public static int byExecutorService() {
        int result = 0;
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();//这是得到的返回值
            }
        });
        try {
            result = (int) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }






    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
