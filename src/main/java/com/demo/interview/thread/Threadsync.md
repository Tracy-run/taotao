# 线程同步的方法


## 1.使用synchronized关键字 
    由于java的每个对象都有一个内置锁，当用此关键字修饰方法时， 内置锁会保护整个方法。
    在调用该方法前，需要获得内置锁，否则就处于阻塞状态。
    注： synchronized关键字也可以修饰静态方法，此时如果调用该静态方法，将会锁住整个类。
    
    注：同步是一种高开销的操作，因此应该尽量减少同步的内容。通常没有必要同步整个方法，
        使用synchronized代码块同步关键代码即可。
        同步方法:给一个方法增加synchronized修饰符之后就可以使它成为同步方法，这个方法可以是静态方法和非静态方法，
        但是不能是抽象类的抽象方法，也不能是接口中的接口方法
        
    同步块：同步块是通过锁定一个指定的对象，来对同步块中包含的代码进行同步；而同步方法是对这个方法块里的代码进行同步，
    而这种情况下锁定的对象就是同步方法所属的主体对象自身。如果这个方法是静态同步方法呢？
    那么线程锁定的就不是这个类的对象了，也不是这个类自身，而是这个类对应的java.lang.Class类型的对象。
    同步方法和同步块之间的相互制约只限于同一个对象之间，所以静态同步方法只受它所属类的其它静态同步方法的制约，
    而跟这个类的实例（对象）没有关系。
                                     
    如果一个对象既有同步方法，又有同步块，那么当其中任意一个同步方法或者同步块被某个线程执行时，这个对象就被锁定了，
    其他线程无法在此时访问这个对象的同步方法，也不能执行同步块。
    
    
    public class MybanRunnable implements Runnable{     
    	private Bank bank;    	
    	public MybanRunnable(Bank bank) {
    		this.bank = bank;
    	}
    	@Override
    	public void run() {
    		for(int i=0;i<10;i++) {
    			bank.save1(100);
    			System.out.println("账户余额是---"+bank.getAccount());
    		}
    	}
    }
    
    class Bank{
    	private int account = 100;    	
    	public int getAccount() {
    		return account;
    	}
    	//同步方法
    	public synchronized void save(int money) {
    		account+=money;
    	}    	
    	public void save1(int money) {
    		//同步代码块
    		synchronized(this) {
    			account+=money;
    		}    		
    	}    	
    	public void userThread() {
    		Bank bank = new Bank();
    		
    		MybanRunnable my1 = new MybanRunnable(bank);
    		System.out.println("线程1");
    		Thread th1 = new Thread(my1);
    		th1.start();
    		System.out.println("线程2");
    		Thread th2 = new Thread(my1);
    		th2.start();
    	}
    }
    
 ##2.wait和notify   
    
    wait():使一个线程处于等待状态，并且释放所持有的对象的lock。
    sleep():使一个正在运行的线程处于睡眠状态，是一个静态方法，调用此方法要捕捉InterruptedException异常。
    notify():唤醒一个处于等待状态的线程，注意的是在调用此方法的时候，并不能确切的唤醒某一个等待状态的线程，而是由JVM确定唤醒哪个线程，而且不是按优先级。
    Allnotity():唤醒所有处入等待状态的线程，注意并不是给所有唤醒线程一个对象的锁，而是让它们竞争
 
##3.使用特殊域变量volatile实现线程同步 
 
    a.volatile关键字为域变量的访问提供了一种免锁机制
    b.使用volatile修饰域相当于告诉虚拟机该域可能会被其他线程更新
    c.因此每次使用该域就要重新计算，而不是使用寄存器中的值 
    d.volatile不会提供任何原子操作，它也不能用来修饰final类型的变量 
    
    注：多线程中的非同步问题主要出现在对域的读写上，如果让域自身避免这个问题，则就不需要修改操作该域的方法。 
        用final域，有锁保护的域和volatile域可以避免非同步的问题。
    
        //只给出要修改的代码，其余代码与上同 
        class Bank {
            //需要同步的变量加上volatile
            private volatile int account = 100;
 
            public int getAccount() {
                return account;
            }
            //这里不再需要synchronized 
            public void save(int money) {
                account += money;
            }
        ｝
    
    
## 4.使用重入锁实现线程同步    
     
    在JavaSE5.0中新增了一个java.util.concurrent包来支持同步。 
      ReentrantLock类是可重入、互斥、实现了Lock接口的锁，它与使用synchronized方法和快具有相同的基本行为和语义，并且扩展了其能力。
    ReenreantLock类的常用方法有：
        ReentrantLock() : 创建一个ReentrantLock实例 
        lock() : 获得锁 
        unlock() : 释放锁
    
    注：ReentrantLock()还有一个可以创建公平锁的构造方法，但由于能大幅度降低程序运行效率，不推荐使用 
    private int account = 100;
    	private ReentrantLock lock = new ReentrantLock();
    	public int getAccount() {
    		return account;
    	}
    	//同步方法
    	public  void save(int money) {
    		lock.lock();
    		try {
    			account+=money;
    		} finally {
    			lock.unlock();
    		}
    		
    	}
    注：关于Lock对象和synchronized关键字的选择： 
    a.最好两个都不用，使用一种java.util.concurrent包提供的机制，能够帮助用户处理所有与锁相关的代码。 
    b.如果synchronized关键字能满足用户的需求，就用synchronized，因为它能简化代码 
    c.如果需要更高级的功能，就用ReentrantLock类，此时要注意及时释放锁，否则会出现死锁，
    通常在finally代码释放锁 
    
    
##5.使用局部变量来实现线程同步

    如果使用ThreadLocal管理变量，则每一个使用该变量的线程都获得该变量的副本，副本之间相互独立，
    这样每一个线程都可以随意修改自己的变量副本，而不会对其他线程产生影响。
    ThreadLocal 类的常用方法

        ThreadLocal() : 创建一个线程本地变量 
        get() : 返回此线程局部变量的当前线程副本中的值 
        initialValue() : 返回此线程局部变量的当前线程的"初始值" 
        set(T value) : 将此线程局部变量的当前线程副本中的值设置为value
    
    //只改Bank类，其余代码与上同
    public class Bank{
        //使用ThreadLocal类管理共享变量account
        private static ThreadLocal<Integer> account = new ThreadLocal<Integer>(){
            @Override
            protected Integer initialValue(){
                return 100;
            }
        };
        public void save(int money){
            account.set(account.get()+money);
        }
        public int getAccount(){
            return account.get();
        }
    }
    注：ThreadLocal与同步机制 
        a.ThreadLocal与同步机制都是为了解决多线程中相同变量的访问冲突问题。 
        b.前者采用以"空间换时间"的方法，后者采用以"时间换空间"的方式
        


## 6.使用阻塞队列实现线程同步      
    
    前面5种同步方式都是在底层实现的线程同步，但是我们在实际开发当中，应当尽量远离底层结构。 
    使用javaSE5.0版本中新增的java.util.concurrent包将有助于简化开发。 本小节主要是使用LinkedBlockingQueue<E>来实现线程的同步
     LinkedBlockingQueue<E>是一个基于已连接节点的，范围任意的blocking queue。 队列是先进先出的顺序（FIFO），
     关于队列以后会详细讲解~LinkedBlockingQueue 类常用方法 LinkedBlockingQueue() : 创建一个容量为Integer.MAX_VALUE的
     LinkedBlockingQueue put(E e) : 在队尾添加一个元素，如果队列满则阻塞 size() : 返回队列中的元素个数 take() : 
     移除并返回队头元素，如果队列空则阻塞代码实例： 实现商家生产商品和买卖商品的同步
   
    注：BlockingQueue<E>定义了阻塞队列的常用方法，尤其是三种添加元素的方法，我们要多加注意，当队列满时：   
    add()方法会抛出异常   
    offer()方法返回false   
    put()方法会阻塞   
   
##7.使用原子变量实现线程同步

  需要使用线程同步的根本原因在于对普通变量的操作不是原子的。
  
  那么什么是原子操作呢？原子操作就是指将读取变量值、修改变量值、保存变量值看成一个整体来操作即-这几种行为要么同时完成，
  要么都不完成。在java的util.concurrent.atomic包中提供了创建了原子类型变量的工具类，使用该类可以简化线程同步。
  其中AtomicInteger 表可以用原子方式更新int的值，可用在应用程序中(如以原子方式增加的计数器)，但不能用于替换Integer；
  可扩展Number，允许那些处理机遇数字类的工具和实用工具进行统一访问。
  
  AtomicInteger类常用方法：  
  AtomicInteger(int initialValue) : 创建具有给定初始值的新的  
  AtomicIntegeraddAddGet(int dalta) : 以原子方式将给定值与当前值相加  
  get() : 获取当前值  
  代码实例：  
  只改Bank类，其余代码与上面第一个例子同
  
      class Bank {
          private AtomicInteger account = new AtomicInteger(100);
          public AtomicInteger getAccount() {
              return account; 
          } 
          public void save(int money) {
              account.addAndGet(money);
          }
      }
 