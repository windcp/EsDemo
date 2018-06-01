package es;


import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import util.CarCard;
import util.IDCardNo;
import util.RandomValue;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * es5.6.8客户端连接方法
 */
public class EsTest {

    private static  TransportClient client = null;

    public static void main(String[] args) {
        try {

            //设置集群名称

            Settings settings = Settings.builder().put("cluster.name","zishuo").put("client.transport.sniff", true).build();
            //创建client
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.16.2.118"), 9300));
            //写入数据
            // createDate(client);
            //搜索数据
            //GetResponse response = client.prepareGet("logstash-2015.05.18", "log", "AWNOD9bns3DZpgUXZE5T").execute().actionGet();
            //输出结果
            //System.out.println(response.getSource());

            String indexName = "car";
            //String indexName = "wind02";
            //创建索引
            //createIndex(indexName);
            //createIndexNameNew(indexName);
            //添加mapping
            //createMapping(indexName);
            //删除索引
            //deleteIndex(indexName);
            //批量添加数据
            createManyData(indexName);
            //添加数据
            //createDate(indexName);
            //修改数据
            //updateDate(indexName);
            //查询数据
            //queryDate(indexName);
            //删除数据
            //deleteDate(indexName);
            //查询所有数据
            //EsQuery.matchAllQuery(client, indexName);
            //查询_type下的所有数据
            //EsQuery.matchQueryForType(client, indexName);
            //模糊查询
            //EsQuery.matchQuery(client,indexName);
            //term查询；精确匹配
            //注：汉字被拆分成一个字，英文的话每一个空格代表一个单词相隔
            // EsQuery.termQuery(client, indexName);
            //多字段精确匹配
            //EsQuery.termsQuerys(client, indexName);
            //范围查询
            //EsQuery.rangeQuerys(client, indexName);
            //EsQuery.multiMatchQuery(client, indexName);
            //id查询
            //EsQuery.MultiGetResponse(client, indexName);
            //EsQuery.idsQuery(client, indexName);
            //
            //EsQuery.queryStringQuery(client, indexName);
            //前缀匹配
            //EsQuery.prefixQuery(client, indexName);
            //通配符匹配查询
            //EsQuery.wildcardQuery(client, indexName);
            //模糊查询
            //EsQuery.fuzzyQuery(client, indexName);
            //多条件查询
            //EsQuery.multiSearchResponse(client, indexName);
            //bool查询
            //EsQuery.boolQuery(client, indexName);
            //关闭client
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建索引
     */
    public static void createIndexNameNew(String indices){
        client.admin().indices().prepareCreate(indices).execute().actionGet();
    }

    /**
     * 创建mapping
     */
    public static void createMapping(String indices)throws Exception{
        new XContentFactory();
        XContentBuilder builder=XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("plateNum").field("type","string").field("store","yes").field("analyzer","ik_max_word")
                .endObject()
                .startObject("owner").field("type","string").field("store","yes").field("index","not_analyzed")
                .endObject()
                .startObject("phone").field("type","string")
                .field("store","yes")
                .endObject()
                .startObject("IDCard").field("type","string")
                .field("store","yes")
                .endObject()
                .endObject()
                .endObject();
        PutMappingRequest mapping = Requests.putMappingRequest(indices).type("cartest").source(builder);
        client.admin().indices().putMapping(mapping).actionGet();
    }

    /**
     * 批量添加数据
     * @param indexName
     */
    public static void createManyData(String indexName){
        long start = System.currentTimeMillis();
        System.out.println(start);
        int count = 1000000;
        String type = "cartest";
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        Map<String, Object> map = new HashMap<String, Object>();
        for(int i = 0;i <= count;i++){
            String carNum =  CarCard.getCarNum();
            String name = RandomValue.getChineseName();
            String phone = RandomValue.getTel();
            String idCard = IDCardNo.getRandomID();
            map.put("plateNum", carNum);
            map.put("owner", name);
            map.put("phone", phone);
            map.put("IDCard", idCard);
            bulkRequestBuilder.add(client.prepareIndex(indexName, type, UUID.randomUUID().toString().replaceAll("-","")).setSource(map));
            if(i%500000 ==0){
                bulkRequestBuilder.execute().actionGet();
                long end = System.currentTimeMillis();
                System.out.println(end-start);
                start = end;
            }
        }
    }

    /**
     * 删除数据
     * @param indexName
     */
    public static void deleteDate(String indexName){
        DeleteResponse deleteResponse = client.prepareDelete(indexName,"article", "80c57588-083f-45bb-8291-af78df8cd586").get();
        boolean result = deleteResponse.isFragment();
        if (!result){
            System.out.println("删除成功");
        }else{
            System.out.println("删除失败：文档不存在");
        }
    }

    /**
     * 查询数据
     * @param indexName
     */
    public static void queryDate(String indexName){
        GetResponse response = client.prepareGet(indexName,"article", "80c57588-083f-45bb-8291-af78df8cd586").get();
        String surce = response.getSource().toString();
        System.out.println(surce);
    }

    /**
     * 更新数据
     * @param indexName
     */
    public static void updateDate(String indexName){
        UpdateRequest updateRequest = null;
        try {
            updateRequest = new UpdateRequest(indexName, "article", "80c57588-083f-45bb-8291-af78df8cd586")
                    .doc(jsonBuilder().startObject().field("eventCount", "3").endObject());
            client.update(updateRequest).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    /**
     * 添加数据
     * @param indexName
     */
    public static void createDate(String indexName){
        IDCardNo idCardNo = new IDCardNo();
        for (int i = 0; i<100000; i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("plateNum", CarCard.getCarNum());
            map.put("owner", RandomValue.getChineseName());
            map.put("phone", RandomValue.getTel()) ;
            map.put("IDCard", idCardNo.getRandomID());
            try {
                IndexResponse response = client.prepareIndex(indexName, "cartest",UUID.randomUUID().toString().replaceAll("-",""))
                        .setSource(map).execute().actionGet();
                if(i%1000 == 0){
                    System.out.println("写入数据结果=" + response.status().getStatus() + "！1000条" );
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    /**
     * 创建索引
     */
    public static void createIndex(String indexName){
        try {
            XContentBuilder mapping = jsonBuilder()
                    /*.startObject()
                       .startObject("settings")
                          .field("number_of_shards",5)  //分片数
                          .field("number_of_replicas",0) //备份数：如果只有一台机器，设置为0
                       .endObject()
                    .endObject()*/
                    .startObject()
                        .startObject("properties")
                            .startObject("type").field("type","string").field("store","yes").field("analyzer","ik_max_word")
                            .endObject()
                            .startObject("eventCount").field("type","long").field("store","yes")
                            .endObject()
                            .startObject("eventDate").field("type","date").field("format","dateOptionalTime")
                               .field("store","yes")
                            .endObject()
                            .startObject("message").field("type","string").field("index","not_analyzed")
                               .field("store","yes")
                            .endObject()
                         /*  .startObject("english_name").field("type","string").field("analyzer","english")
                           .endObject()*/
                      .endObject()
                    .endObject();
            CreateIndexRequestBuilder  cib=client.admin()
                    .indices().prepareCreate(indexName).addMapping("iktest", mapping);
            CreateIndexResponse response = cib.execute().actionGet();


            if(response.isAcknowledged()){
                System.out.println("index.created");
            }else {
                System.out.println("index.created.failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     */
    public static void deleteIndex(String indexName){
        DeleteIndexRequest delete = new DeleteIndexRequest(indexName);
        client.admin().indices().delete(delete);
    }

}
