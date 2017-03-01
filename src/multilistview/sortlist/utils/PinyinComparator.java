package multilistview.sortlist.utils;

import java.util.Comparator;
import multilistview.sortlist.vo.BaseFilterEntity;


/**
 * 排序的拼音比较器
 * @author Jack
 */
public class PinyinComparator implements Comparator<BaseFilterEntity> {

    public int compare(BaseFilterEntity o1, BaseFilterEntity o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.getSortLetter().equals("#")) {
            return -1;
        } else if (o1.getSortLetter().equals("#")) {
            return 1;
        } else {
            return o1.getSortLetter().compareTo(o2.getSortLetter());
        }
    }
}