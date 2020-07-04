package xin.qicloud.es_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qiyb
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JdGoodsInfo implements Serializable {

    private String title;
    private String price;
    private String img;
}
