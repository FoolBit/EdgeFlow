# 代码注解

## utils

### SysUtils

见代码注释



### ObjectUtils

见代码注释



### FileUtils

见代码注释



### DataUtils

[前置知识](#thread)

`DataQueue`在这里实现了`monitor`的机制，用来充当数据队列

`computeThread`在这里充当消费者，从队列中提取数据进行处理

`putdataThread`在这里当生产者，将数据放到队列当中

每当接收到一个数据时，会调用`pudataThread`将数据放入队列，然后`notify` `computeThread`执行任务

为每一个ed节点创建一个`DataQueue`实例，并生成相应的生产者和消费者线程。这样不同ed的线程之间不会冲突，同一个ed的生产和消费之间会形成等待关系。为了更方便的进行cpu限制，这里希望生产数据时是等待关系，防止对同一个ed执行多个任务，所以加了一个`DataQueue_`来实现普通的进程锁，这样生产数据时是一个依次进行的过程。



## thrift

### serverConnect

### serverConnectImpl

见代码注释

```thrift
service serverConnect{
    i32 connect(1:string server)
    bool sendCommand(1:i32 cmd 2:string arg)
    bool uploadFile(1:string data 2:i32 id)
    bool executeTask(1:string data)
}
```

## servers

### Server



# Java

## 多线程

<span id = "thread"></span>

若一个类声明了`implement Runnable`，则该类可以通过`Thread`新建一个线程来执行。类中需要重载`public void run()`函数，线程执行时会自动调用`run`方法



``` java
  public  static class MyThread implements Runnable
    {

        @Override
        public void run() {
            // do somgthing
        }
     }
     
Thread thread = new Thread(new MyThread());
thread.start();
```

## 线程锁

### synchronized

`synchronized`修饰词可以实现线程锁

当用`synchronized`修饰类中的几个方法时

```java

class AccountingSyncClass implements Runnable {
	static int i = 0;

	public  synchronized void increase() {
		i++;
	}
 
	// 非静态方法，锁定当前实例对象
	public synchronized void increase2() {
		i++;
	}

}
```

两个线程分别调用`increse`和`increase2`时会互斥，只有一个执行结束后才能执行另一个线程

该进程锁是针对对象实例锁定的，只有执行同一个对象的两种方法时会互斥，两个不同的实例对象之间不会互斥

### monitor

仅仅是互斥不太好用，有时候希望实现生产者-消费者模式

> 有生产者A和消费者B，正常情况下两者挂起，A每生产一个物品后B都能及时进行消费

一种思想是B线程不断检测，看A有没有进行生产，但消耗太大

可以利用java的`monitor`机制:

```java
public void synchronized consume {

    while (<condition does not hold>)

        wait();

     // perform actions to condition

}

public void synchronized produce {

    // do somethihng

        notify();

     // perform actions to condition

}
```

线程执行到`wait`时会暂停，直到另一个进程执行`notify`

