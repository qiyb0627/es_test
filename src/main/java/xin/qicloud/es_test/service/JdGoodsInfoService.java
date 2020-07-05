package xin.qicloud.es_test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qiyb
 */
public interface JdGoodsInfoService {

    /**
     * 根据keyword，查找元素，添加到es中
     *
     * @param keyWord
     * @return
     * @throws Exception
     */
    public Boolean parseContent(String keyWord) throws  Exception;

    /**
     * 根据keyword，查询es库，返回数据
     * @param keyWord
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>>  searchPage(String keyWord ,int pageNo , int pageSize) throws  Exception;
}
