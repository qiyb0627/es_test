package xin.qicloud.es_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xin.qicloud.es_test.service.JdGoodsInfoService;

import java.util.List;
import java.util.Map;

/**
 * @author qiyb
 */
@RestController
public class JdGoodsInfoController {

    @Autowired
    private JdGoodsInfoService jdGoodsInfoService;

    @GetMapping(value = "parseContent/{keyWord}")
    public  Boolean parseContent(@PathVariable("keyWord") String keyWord) throws Exception {
        return jdGoodsInfoService.parseContent(keyWord);
    }

    @GetMapping(value = "searchPage/{keyWord}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> searchPage(@PathVariable("keyWord") String keyWord ,
                                               @PathVariable("pageNo") int pageNo ,
                                               @PathVariable("pageSize") int pageSize) throws Exception {
        return jdGoodsInfoService.searchPage(keyWord, pageNo, pageSize);
    }

}
