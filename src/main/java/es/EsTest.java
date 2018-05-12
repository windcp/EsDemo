package es;


import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class EsTest {
    public static void main(String[] args) {
        try {

            //设置集群名称

            Settings settings = Settings.builder().put("cluster.name","zishuo").put("client.transport.sniff", true).build();
            //创建client
            @SuppressWarnings("resource")
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.16.2.117"), 9300));
            //写入数据
            // createDate(client);
            //搜索数据
            GetResponse response = client.prepareGet("shakespeare", "doc", "1").execute().actionGet();
            //输出结果
            System.out.println(response.getSource());

            //关闭client
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
