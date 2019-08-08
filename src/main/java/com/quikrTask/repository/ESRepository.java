package com.quikrTask.repository;

import com.quikrTask.model.ESUserCommentModel;
import com.quikrTask.utils.JsonUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ESRepository {

    @Autowired
    private RestHighLevelClient restClient ;
    Logger logger = LoggerFactory.getLogger(ESRepository.class);

        public void index(String indexName, String document){
        try {
            IndexRequest indexRequest = new IndexRequest(indexName, "_doc")
                    .source(document, XContentType.JSON);
            IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);
            logger.info("ESIndex {} {}", indexName, indexResponse.getResult());
        }catch(Exception e){
            logger.error("exception in ESIndex");
        }
    }

    public List<ESUserCommentModel> search(String matchPhrase){
        try {
            SearchRequest searchRequest = new SearchRequest("user_comment");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("userComment", matchPhrase); --> tried this query earlier which was boosting documents by default logic

            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder() ; // custom boosting logic applied here
            boolQueryBuilder.must(QueryBuilders.matchQuery("userComment", matchPhrase).boost(1)) ;
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("userComment", matchPhrase).boost(100)) ;
            boolQueryBuilder.should(QueryBuilders.matchQuery("userComment", matchPhrase).operator(Operator.AND).boost(10)) ;
            searchSourceBuilder.query(boolQueryBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse response = restClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHit = response.getHits().getHits();
            List<ESUserCommentModel> esUserCommentModelList = new ArrayList<>() ;
            for (SearchHit hit : searchHit){
                esUserCommentModelList.add(JsonUtils.fromJsonString(ESUserCommentModel.class, hit.getSourceAsString())) ;
            }
            return esUserCommentModelList ;
        }catch(Exception e){
            logger.error("exception in ES search");
        }
        return null ;
    }
}
