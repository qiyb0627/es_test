package xin.qicloud.es_test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.qicloud.es_test.pojo.JdGoodsInfo;
import xin.qicloud.es_test.utils.HtmlparseUtil;

import java.util.List;

@Service
public class JdGoodInfoService implements JdGoodsInfoService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Override
    public Boolean parseContent(String keyWord) throws Exception {
        List<JdGoodsInfo> jdCommodityInfo = new HtmlparseUtil().getJdCommodityInfo(keyWord);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("3m");
        for (int i = 0; i < jdCommodityInfo.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("jd_goods")
                            .source(JSON.toJSONString(jdCommodityInfo.get(i)),XContentType.JSON
                            ));
        }
        BulkResponse rsBulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !rsBulk.hasFailures();
    }
}
