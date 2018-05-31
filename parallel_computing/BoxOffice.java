package hw1;
import java.util.concurrent.*;
public class BoxOffice extends Thread{
	static private volatile Integer num=30;
	private String name;
	public BoxOffice(String s){
		name=s;
	}
	public void run(){
		while(true){
			synchronized(num){
				if(num<1){
					System.out.println(name+": NO tickets");
					break;
				}	
				else{
					System.out.println(Thread.currentThread().getName()+": "+name+"����"+num+"��");
					num--;
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
	public static void main(String args[]){
			BoxOffice a=new BoxOffice("����1"),b=new BoxOffice("����2"),c=new BoxOffice("����3"),d=new BoxOffice("����4");
			a.start();b.start();c.start();d.start();
	}
}
