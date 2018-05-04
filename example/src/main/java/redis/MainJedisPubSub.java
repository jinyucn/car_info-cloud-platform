package redis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import data.Record;
import hbase.HBaseInsert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

public class MainJedisPubSub extends JedisPubSub {
    static Gson gson = new Gson();
    private static HBaseInsert in = new HBaseInsert();
    List<Record> records = new ArrayList<>(500000);
    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        Record record;

        try {
            record = gson.fromJson(message,Record.class);
            System.out.println("record"+record.toString());
            records.add(record);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.out.println("record+"+message);
            in.insertRecordsToHBase(records);
        }
    }

    public static void main(String[] args){
        try {
            Jedis jr = null;
            jr = new Jedis("192.168.31.112",6379,0);
            MainJedisPubSub sp = new MainJedisPubSub();
            sp.proceed(jr.getClient(),"news.share");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
