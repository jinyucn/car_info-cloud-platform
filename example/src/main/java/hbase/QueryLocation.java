package hbase;

import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
public class QueryLocation {
    private final String RECORD_TABLENAME = "Trace1";
    private final String FAMILY_NAME = "location";
    int time;String longitude,latitude,car_time;
    public boolean query() {
        Table table = HBaseConf.getTableByName(RECORD_TABLENAME);
        System.out.println("请输入查询车牌号：");
        Scanner eid1=new Scanner(System.in);
        String eid=eid1.next();
        System.out.println("请输入开始查询时间：");
        Scanner start_time1=new Scanner(System.in);
        String start_time=eid1.next();
        System.out.println("请输入终止查询时间：");
        Scanner stop_time1=new Scanner(System.in);
        String stop_time=eid1.next();
        try {
            Scan s =  new  Scan();
            s.setStartRow(Bytes.toBytes(eid+"##"+start_time));
            s.setStopRow(Bytes.toBytes(eid+"##"+stop_time+"0"));
            ResultScanner rs = table.getScanner(s);
            System.out.println("时间        经度      纬度");
            for  (Result r : rs) {
                String row = Bytes.toString(r.getRow());time=1;
                for (Cell cell : r.rawCells()) {
                   if (time % 2 == 1) {
                        longitude = Bytes.toString(CellUtil.cloneValue(cell));
                        time++;
                    } else {
                       car_time = row.split("##")[1];
                        latitude = Bytes.toString(CellUtil.cloneValue(cell));
                        System.out.println(car_time+" "+longitude + " " + latitude);
                    }
                }
            }
            table.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
