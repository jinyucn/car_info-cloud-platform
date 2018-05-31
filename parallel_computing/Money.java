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
					System.out.println(Thread.currentThread().getName()+": "+zhang+"ȡ��100Ԫ��ʣ��"+money+"Ԫ");
				}
				else{
					System.out.println(Thread.currentThread().getName()+": ����ȡǮ������!");
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
					System.out.println(Thread.currentThread().getName()+": "+li+"ȡ��50Ԫ��ʣ��"+money+"Ԫ");
				}
				else{
					System.out.println(Thread.currentThread().getName()+": ����ȡǮ������!");
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
		Bank zhangsan=new Zhang("����");
		Bank Lisi=new Li("����");
		zhangsan.start();
		Lisi.start();
	}

}
