package main;

import com.google.gson.Gson;
import data.Record;
import hbase.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {


	private static String key = "rdb";//redis key
	private static String  redisHost = "192.168.31.112";
	private static int  redisPort = 6379;
	private static String topicName = "test-topic";
	private static String kafkaClusterIP = "192.168.2.101:9092,192.168.2.102:9092";
	private static String recordFilePath = "./data/record.json";

	private static Jedis jedis = new Jedis(redisHost,redisPort);
	private static HBaseInsert in = new HBaseInsert();


	static void jsonToKafka() throws IOException {
		Properties props = new Properties();
		props.put("bootstrap.servers", kafkaClusterIP);//kafka clusterIP
		props.put("acks", "1");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(props);
		BufferedReader br =  new BufferedReader(new FileReader(recordFilePath));
		int i = 0;//record key
		String record;
		//send record to kafka
		while((record = br.readLine())!=null) {
			producer.send(new ProducerRecord<String, String>(topicName, Integer.toString(i), record), new Callback() {
				public void onCompletion(RecordMetadata metadata, Exception e) {
					if (e != null)
						e.printStackTrace();
					System.out.println("The offset of the record we just sent is: " + metadata.offset());
				}
			});
			i++;
		}
		producer.close();
	}

	static void kafkaToRedis(){
		Properties props = new Properties();
		Pipeline pipelineq = jedis.pipelined();
		props.put("bootstrap.servers", kafkaClusterIP);//kafka clusterIP
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topicName));

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records){
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
				pipelineq.sadd(key,record.value());//record to redis
			}

		}
	}


	static void redisToHbase() throws IOException {
		List list = new ArrayList();
		Record record;
		Gson gson = new Gson();
		for(String jsonString : jedis.smembers(key)){
			System.out.println(jsonString);
			record = gson.fromJson(jsonString,Record.class);
			System.out.println(record.getAddress());
			list.add(record);
		}
		in.insertRecordsToHBase(list);
	}
	static void tracebuild() throws IOException {
		//MaptraceBuild a=new MaptraceBuild();
		//a.BuildTraceTable();
		maptrace a=new maptrace();
		a.BuildTraceTable();
	}
	static void querylocation() throws IOException {
		QueryLocation a=new QueryLocation();
		a.query();
	}
	static void filterquery() throws IOException {
		FilterTest a=new FilterTest();
		a.query();
	}
	public static void main(String[] args) throws IOException {


		/*
		1.发送record至kafka
		 */
		//jsonToKafka();
		/*
		2.将kafka中的信息写入redis
		 */
		//kafkaToRedis();
		/*
		3.在HBase中创建数据库(创建一次)
		 */
		//HBaseCreateOP.main(args);
		/*
		4.将redis中的数据发送至HBase
		 */
		//redisToHbase();
		//5、插入坐标表
		//tracebuild();
		//6、查询坐标表
		//querylocation();
		//7、过滤器查询次数
		filterquery();
	}
}
