package hw2;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
class ForkJoinTest extends RecursiveTask<Integer>{
	private int threshold;
	private List<String> list;
	public ForkJoinTest(List<String> list, int threshold) {
		this.list = list;
		this.threshold = threshold;
	}
	protected Integer compute(){
		if(list.size()<threshold){
			Integer sum=0;
			for(String i:list){
				if(i.matches("[0-9]+")){
					sum+=Integer.parseInt(i);
				}
			}
			return sum;
		}
		else{
			int mid=list.size()/2;
			List<String> leftList=list.subList(0,mid);
			List<String> rightList=list.subList(mid, list.size());
			ForkJoinTest left=new ForkJoinTest(leftList,threshold);
			ForkJoinTest right=new ForkJoinTest(rightList,threshold);
			left.fork();
			right.fork();
			Integer join=left.join();
			join+=right.join();
			return join;
		}
	}
}
public class Tester {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] l1={"&","^","2","*","7"};
		String[] l2={"(","3",".","1",";","9"};
		List<String> list_1=new ArrayList<>(Arrays.asList(l1));
		List<String> list_2=new ArrayList<>(Arrays.asList(l2));
		ForkJoinPool pool=new ForkJoinPool();
		ForkJoinTest task1=new ForkJoinTest(list_1,2);
		ForkJoinTest task2=new ForkJoinTest(list_2,2);
		Future<Integer> result1=pool.submit(task1);
		Future<Integer> result2=pool.submit(task2);
		try {
			int result=result1.get();
			int result_2=result2.get();
			System.out.println(result);
			System.out.println(result_2);
			if(compare(result,result2))
				System.out.println("数组1大");
			else if(!compare(result,result2))
				System.out.println("数组2大");
			else
				System.out.println("一样大");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pool.shutdown();
	}

	private static boolean compare(int result, Future<Integer> result2) {
		// TODO Auto-generated method stub
		return false;
	}

}
