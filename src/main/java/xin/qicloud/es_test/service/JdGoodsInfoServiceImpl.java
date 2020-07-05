package xin.qicloud.es_test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.qicloud.es_test.pojo.JdGoodsInfo;
import xin.qicloud.es_test.utils.HtmlparseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author qiyb
 */
@Service
public class JdGoodsInfoServiceImpl implements JdGoodsInfoService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Override
    public Boolean parseContent(String keyWord) throws Exception {
        //发送爬虫请求，返回数据
        List<JdGoodsInfo> jdCommodityInfo = new HtmlparseUtil().getJdCommodityInfo(keyWord);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("3m");
        for (int i = 0; i < jdCommodityInfo.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("jd_goods")
                            .source(JSON.toJSONString(jdCommodityInfo.get(i)),XContentType.JSON
                            ));
        }
        //入库
        BulkResponse rsBulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !rsBulk.hasFailures();
    }

    @Override
    public List<Map<String, Object>> searchPage(String keyWord, int pageNo, int pageSize) throws Exception {
        //设置 返回对象
        List<Map<String,Object>> rsList = new ArrayList<>();

        //条件查询
        SearchRequest jdGoodsSearchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //判断分页信息
        pageSize = pageSize == 0 ? 10:pageSize;
        pageNo = pageNo <= 1 ? 1:pageNo;
        //设置分页信息
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        //精准匹配
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", keyWord);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.timeout(new TimeValue(300, TimeUnit.SECONDS));

        //高亮显示，高亮的部分，是否全部高亮，高亮部分开始前标签，结束标签
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style = 'coolr = red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        //实行搜素
        jdGoodsSearchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(jdGoodsSearchRequest, RequestOptions.DEFAULT);

        //解析结果
        for(SearchHit searchHit:searchResponse.getHits().getHits()){
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField oldTitle = highlightFields.get("title");
            //原来的结果
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            if(oldTitle != null){
                Text[] fragments = oldTitle.getFragments();
                String newTitle = "" ;
                for (Text text : fragments) {
                    newTitle += text;
                }
                sourceAsMap.put("title", newTitle);
            }
            rsList.add(searchHit.getSourceAsMap());
        }


        return rsList;
    }

}
