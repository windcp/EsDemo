package es;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 * 查询测试
 */
public class EsQuery {



    /**
     * bool
     * @param client
     * @param indexName
     */
    public static void  boolQuery(TransportClient client ,String indexName){
          QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                  .must(QueryBuilders.termQuery("type", "红"))
                  .mustNot(QueryBuilders.termQuery("type", "西")) //添加不得出现在匹配文档中的查询。
                  .should(QueryBuilders.termQuery("type", "明"))//添加应该与返回的文档匹配的子句。 对于具有no的布尔查询,子句必须一个或多个SHOULD子句且必须与文档匹配,用于布尔值查询匹配。 不允许null值。
                  .filter(QueryBuilders.termQuery("type", "红"));//添加一个查询，必须出现在匹配的文档中，但会不贡献得分。 不允许null值。
        SearchResponse response = client.prepareSearch(indexName).setTypes("article").setQuery(queryBuilder).execute().actionGet();
        for (SearchHit searchHit : response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     * 查询该index下的所有数据
     * @param client
     * @param indexName
     */
    public static void  matchAllQuery(TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit searchHit : response.getHits()) {
            System.out.println(searchHit);
        }
    }

    /**
     * 查询_index,_type下的所有数据
     * @param client
     * @param indexName
     */
    public static void  matchQueryForType(TransportClient client ,String indexName){
        SearchResponse response = client.prepareSearch(indexName).setTypes("article").get();
        System.out.println(response);
        for (SearchHit searchHit : response.getHits()) {
            System.out.println(searchHit);
        }
    }

    /**
     * 模糊匹配查询
     * @param client
     * @param indexName
     */
    public static void  matchQuery(TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(
                "type" ,
                "西游"
        );
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit searchHit : response.getHits()) {
            System.out.println(searchHit);
        }
    }

    /**
     * multiMatchQuery 查询
     * multiMatchQuery针对的是多个field,当fieldNames有多个参数时，
     * 如field1和field2，那查询的结果中，要么field1中包含text，要么field2中包含text。
     */
    public static void  multiMatchQuery(TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("我","message", "type");
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     * 精确匹配
     * @param client
     * @param indexName
     */
    public static void termQuery(TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.termQuery("type","西");
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     * 多字段精确匹配
     * @param client
     * @param indexName
     */
    public static void termsQuerys(TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.termsQuery("type","西","红");
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     * 范围查询
     * @param client
     * @param indexName
     */
    public static void rangeQuerys(TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("eventCount")
                .from(1)
                .to(3)
                .includeLower(false)
                .includeUpper(true);
        // A simplified form using gte, gt, lt or lte
        QueryBuilder qb = QueryBuilders.rangeQuery("eventCount")
                .gt("1")// >
                .lte("3");//  <=
        SearchResponse response = client.prepareSearch(indexName).setTypes("article","star").setQuery(qb).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     * 根据id获取
     * @param client
     * @param indexName
     */
    public static void MultiGetResponse (TransportClient client ,String indexName){
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add(indexName, "article", "3f8c65cf7d72415db33bb52c05660095", "704dadb4ca2d45bbab2cfd63a8f33e1f")
                .get();
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String json = response.getSourceAsString();
                System.out.println(json);
                System.out.println(response.getSource());
            }
        }
    }

    /**
     * idsQuery
     * 类型是可选的
     * 指定type和id进行查询。
     * @param client
     * @param indexName
     */
    public static void idsQuery (TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.idsQuery("article")
                .addIds("3f8c65cf7d72415db33bb52c05660095", "704dadb4ca2d45bbab2cfd63a8f33e1f");
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     * * +包含 -除外
     * 模糊查询
     * @param client
     * @param indexName
     */
    public static void queryStringQuery (TransportClient client ,String indexName){
        //QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("*:*");
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("+type:红");
        QueryBuilder queryBuilder2 = QueryBuilders.simpleQueryStringQuery("+type:红");
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     *  prefixQuery  匹配包含具有指定前缀的术语的文档的查
     *  匹配分词前缀 如果字段没分词，就匹配整个字段前缀
     * @param client
     * @param indexName
     */
    public static void prefixQuery (TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.prefixQuery(
                "message",
                "我是"
        );
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /*
     * wildcardQuery
     * 通配符
     */
    public static void wildcardQuery (TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("message", "*玉");//J?V*
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /*
     * fuzzyQuery  使用模糊查询匹配文档的查询
     */
    public static void fuzzyQuery (TransportClient client ,String indexName){
        QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery(
                "type",
                "楼"
        );
        SearchResponse response = client.prepareSearch(indexName).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            System.out.println(searchHit.getSource());
        }
    }

    /**
     * 多字段查询
     * @param client
     * @param indexName
     */
    public static void multiSearchResponse(TransportClient client ,String indexName){
        QueryBuilder queryBuilder1 = QueryBuilders.fuzzyQuery(
                "type",
                "楼"
        );
        QueryBuilder queryBuilder2 = QueryBuilders.prefixQuery(
                "message",
                "我是"
        );
        SearchRequestBuilder srb1 = client.prepareSearch().setQuery(queryBuilder1);
        SearchRequestBuilder srb2 = client.prepareSearch().setQuery(queryBuilder2);

        MultiSearchResponse sr = client.prepareMultiSearch().add(srb1).add(srb2).get();

        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            for (SearchHit searchHit : response.getHits()) {
                System.out.println(searchHit.getSource());
            }
        }
    }
}
