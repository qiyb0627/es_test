package xin.qicloud.es_test.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xin.qicloud.es_test.pojo.JdGoodsInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiyb
 */
public class HtmlparseUtil {
    public static void main(String[] args) throws IOException {
        new HtmlparseUtil().getJdCommodityInfo("java").forEach(System.out::println);
    }

    public List<JdGoodsInfo> getJdCommodityInfo(String keyWord) throws IOException {
        List<JdGoodsInfo> goodsList = new ArrayList<>();

        String url = "https://search.jd.com/Search?keyword="+ keyWord;
        Document document = Jsoup.parse(new URL(url), 50000);
        Element goodsListElements = document.getElementById("J_goodsList");
        Elements liElements = goodsListElements.getElementsByTag("li");

        for (Element el :liElements) {
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name p-name-type-2").eq(0).text();
            JdGoodsInfo jdGoodsInfo = new JdGoodsInfo();
            jdGoodsInfo.setImg(img);
            jdGoodsInfo.setPrice(price);
            jdGoodsInfo.setTitle(title);
            goodsList.add(jdGoodsInfo);
        }
        return goodsList;
    }

}
