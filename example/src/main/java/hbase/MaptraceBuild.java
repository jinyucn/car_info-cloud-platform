package hbase;
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
public class MaptraceBuild {
    private final String RECORD_TABLENAME1 = "Record";
    private final String FAMILY_NAME1 = "info";  //给出的用例中只有一个Record表，表中只有一个info列族
    private final String RECORD_TABLENAME = "trace";
    private final String FAMILY_NAME = "location";
    long time=1,all=0;String add;String longitude,latitude;
    public boolean BuildTraceTable() {
        Table table1 = HBaseConf.getTableByName(RECORD_TABLENAME1);
        Table table = HBaseConf.getTableByName(RECORD_TABLENAME);
        try {
            Scan s =  new  Scan();
            ResultScanner rs = table1.getScanner(s);
            for  (Result r : rs) {
                String row = Bytes.toString(r.getRow());
                time = 1;
                for (Cell cell : r.rawCells()) {
                    if (time % 3 == 1) {
                        add = Bytes.toString(CellUtil.cloneValue(cell));
                        time++;
                    } else if (time % 3 == 2) {
                        longitude = Bytes.toString(CellUtil.cloneValue(cell));
                        time++;
                    } else {
                        all++;
                        latitude = Bytes.toString(CellUtil.cloneValue(cell));
                        //String row=Bytes.toString(r.getRow());
                        String placeId = row.split("##")[0];
                        String time = row.split("##")[1];
                        String eid = row.split("##")[2];
                        //插入
                        List<Put> allPuts = new ArrayList<>();
                        String rowKey = eid + "##" + time;
                        System.out.println(rowKey + " " + longitude + " " + latitude);
                        Put put = new Put(Bytes.toBytes(rowKey));
                        //2.添加列
                        put.addColumn(Bytes.toBytes(FAMILY_NAME), Bytes.toBytes("longitude"), Bytes.toBytes(longitude + ""));
                        put.addColumn(Bytes.toBytes(FAMILY_NAME), Bytes.toBytes("latitude"), Bytes.toBytes(latitude + ""));
                        //3.执行数据库插入操作
                        try {
                            System.out.println("put");
                            table.put(put);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
                System.out.println("all");
            }
            table1.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }



        return true;
    }
}
