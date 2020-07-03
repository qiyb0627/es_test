package xin.qicloud.es_test.sort;

import java.util.List;

/**
 * @author Administrator
 */
public class BubbleSort {

    public static  void  sort(int[] in){

        int lastEcchangeIndex = 0;
        int sortBorder = in.length - 1;
        for (int i = 0; i < in.length -1; i++) {
            boolean isSorted = true;
            for (int j = 0; j < sortBorder; j++) {
                int tmp = 0;
                if(in[j] > in[j+1]){
                    tmp = in[j];
                    in[j] = in[j + 1];
                    in[j + 1] = tmp;
                    isSorted = false;
                }
                sortBorder = lastEcchangeIndex;
                if (isSorted){
                    break;
                }
            }
        }

    }

}
