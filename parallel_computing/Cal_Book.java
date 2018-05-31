package hw2;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.io.*;
class ForkJoinTest1 extends RecursiveTask<Integer>{
	private int threshold;
	private List<String> list;
	public ForkJoinTest1(List<String> list, int threshold) {
		this.list = list;
		this.threshold = threshold;
	}
	protected Integer compute(){
		if(list.size()<threshold){
			//System.out.println(list);
			Integer sum=0;
			for(String i:list){
				if(i.equals("book")){
					sum+=1;
				}
			}
			return sum;
		}
		else{
			int mid=list.size()/2;
			List<String> leftList=list.subList(0,mid);
			List<String> rightList=list.subList(mid, list.size());
			ForkJoinTest1 left=new ForkJoinTest1(leftList,threshold);
			ForkJoinTest1 right=new ForkJoinTest1(rightList,threshold);
			left.fork();
			right.fork();
			Integer join=left.join();
			join+=right.join();
			return join;
		}
	}
}
class Read_file{
	public static List<String> read_from_file(){
		List<String> list_1=new ArrayList<>(),list_2=new ArrayList<>();
		try {  
	        File file1 = new File("a.txt");  
	        // 读取文件，并且以utf-8的形式写出去  
	        BufferedReader bufread1;  
	        String read1;  
	        bufread1 = new BufferedReader(new FileReader(file1));  
	        while ((read1 = bufread1.readLine()) != null) {
	            String[] x=read1.split(",");
	            list_1.addAll(Arrays.asList(x));
	        }  
	        bufread1.close();  
	    } catch (FileNotFoundException ex) {  
	        ex.printStackTrace();  
	    } catch (IOException ex) {  
	        ex.printStackTrace();  
	    }
		try {  
	        File file2 = new File("b.txt");  
	        // 读取文件，并且以utf-8的形式写出去  
	        BufferedReader bufread2;  
	        String read2;  
	        bufread2 = new BufferedReader(new FileReader(file2));  
	        while ((read2 = bufread2.readLine()) != null) {
	            String[] x=read2.split(",");
	            list_2.addAll(Arrays.asList(x));
	        }  
	        bufread2.close();  
	    } catch (FileNotFoundException ex) {  
	        ex.printStackTrace();  
	    } catch (IOException ex) {  
	        ex.printStackTrace();  
	    }
		List<String> list=new ArrayList<>();
		list.addAll(list_1);
		list.addAll(list_2);
		return list;
	}
}
public class Cal_Book {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> file_list =new ArrayList<>();
		file_list = Read_file.read_from_file();
		System.out.println(file_list);
		ForkJoinPool pool=new ForkJoinPool();
		ForkJoinTest1 task=new ForkJoinTest1(file_list,3);
		Future<Integer> result=pool.submit(task);
		try {
			Integer sum=result.get();
			System.out.println(sum);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pool.shutdown();
	}
}
