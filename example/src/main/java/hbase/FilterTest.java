package hbase;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
public class FilterTest {
    private final String RECORD_TABLENAME = "MeetCount1";
    private final String FAMILY_NAME = "info";
    private String eid1,eid2;private int count,time=0,a,b;
    private Filter filter;
    public boolean query() {
        Table table = HBaseConf.getTableByName(RECORD_TABLENAME);
        System.out.println("请输入指定次数：");
        Scanner out=new Scanner(System.in);
        a=out.nextInt();
        System.out.println("小于请按0；小于等于请按1；等于请按2；大于等于请按3；大于请按4；");
        Scanner out1=new Scanner(System.in);
        b=out.nextInt();
        try {
            switch (b){
                case 0:filter=new ValueFilter(CompareOp.LESS,new BinaryComparator(Bytes.toBytes(a)));break;
                case 1:filter=new ValueFilter(CompareOp.LESS_OR_EQUAL,new BinaryComparator(Bytes.toBytes(a)));break;
                case 2:filter=new ValueFilter(CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes(a)));break;
                case 3:filter=new ValueFilter(CompareOp.GREATER_OR_EQUAL,new BinaryComparator(Bytes.toBytes(a)));break;
                case 4:filter=new ValueFilter(CompareOp.GREATER,new BinaryComparator(Bytes.toBytes(a)));break;
            }
            Scan scan=new Scan();
            scan.setFilter(filter);
            ResultScanner rs = table.getScanner(scan);
            System.out.println("eid            eid            count");
            for  (Result r : rs) {
                String eid1 = Bytes.toString(r.getRow());
                for (Cell cell : r.rawCells()) {
                    time++;
                    eid2=Bytes.toString(CellUtil.cloneQualifier(cell));
                    count=Bytes. toInt (CellUtil.cloneValue(cell));
                    System.out.println(eid1+" "+eid2+" "+count);
                }
            }
            System.out.println("总数为："+time/2);
            table.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
