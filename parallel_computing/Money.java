package hw1;
import java.util.*;
abstract class Bank extends Thread{
	static protected volatile Integer money=1500;
	public abstract void run();
}
class Zhang extends Bank{
	private String zhang;
	public Zhang(String name){
		zhang=name;
	}
	public void run(){
		while(true){
			synchronized(Bank.class){
				if(money>=100){
					money-=100;
					System.out.println(Thread.currentThread().getName()+": "+zhang+"取了100元，剩余"+money+"元");
				}
				else{
					System.out.println(Thread.currentThread().getName()+": 张三取钱，余额不足!");
					break;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
class Li extends Bank{
	private String li;
	public Li(String name){
		li=name;
	}
	public void run(){
		while(true){
			synchronized(Bank.class){
				if(money>=50){
					money-=50;
					System.out.println(Thread.currentThread().getName()+": "+li+"取了50元，剩余"+money+"元");
				}
				else{
					System.out.println(Thread.currentThread().getName()+": 李四取钱，余额不足!");
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
public class Money {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bank zhangsan=new Zhang("张三");
		Bank Lisi=new Li("李四");
		zhangsan.start();
		Lisi.start();
	}

}
