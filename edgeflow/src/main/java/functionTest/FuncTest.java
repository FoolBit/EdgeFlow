package functionTest;

import java.io.*;
import java.sql.Time;
import java.util.*;

import utils.*;

public class FuncTest implements Serializable{

    public static class A
    {
        public int id=0;
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String filename = "config.properties";
        Properties properties = new Properties();
        InputStream inputStream = FuncTest.class.getClassLoader().getResourceAsStream(filename);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = (String) properties.get("sourcePort");
        System.out.println(result);
    }
}

class PutClass implements Runnable
{
    private MyQueueQueue myQueue;
    private int id;
    public PutClass(MyQueueQueue q, int id_)
    {
        myQueue = q;
        id = id_;
    }

    public void run()
    {
        for(int i=1; i<=5; ++i)
        {
            myQueue.put(i*id);
        }
    }
}

class GetClass implements Runnable
{
    private MyQueue myQueue;
    public GetClass(MyQueue q)
    {
        myQueue = q;
    }

    public void run()
    {
        for(int i=0; i<10; ++i)
        {
            System.out.println(myQueue.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyQueueQueue
{
    private MyQueue myQueue;
    MyQueueQueue(MyQueue q)
    {
        myQueue = q;
    }

    public synchronized void put(int a)
    {
        myQueue.put(a);
    }
}

class MyQueue
{
    private Queue<Integer> q = new LinkedList<>();
    private boolean avaliable = true;

    public synchronized void put(int a)
    {
        while(avaliable == false)
        {
            try {
                System.out.println("Put: wating "+a);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        avaliable = false;
        q.add(a);
        notify();
    }

    public synchronized int get()
    {
        while(avaliable == true)
        {
            try {
                System.out.println("Put: wating");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        avaliable = true;
        int a = q.remove();
        notify();
        System.out.println("Get"+a);

        return a;
    }
}