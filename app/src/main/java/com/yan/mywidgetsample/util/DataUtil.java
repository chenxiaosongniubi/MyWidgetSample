package com.yan.mywidgetsample.util;

import com.yan.mywidgetsample.entity.Data;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * yanweiqiang
 * 2017/11/29.
 */

public class DataUtil {

    public static List<ItemData> getItemDataList(int size) {
        List<ItemData> itemDataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i % 20 == 0) {
                Index index = new Index();
                index.setText("index " + i / 10);
                itemDataList.add(index);
                continue;
            }

            Data data = new Data();
            data.setViewType(1);
            data.setText("data " + i);
            itemDataList.add(data);
        }
        return itemDataList;
    }

    public static List<Data> getDataList(int size) {
        List<Data> dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Data data = new Data();
            data.setViewType(1);
            data.setText("data " + i);
            dataList.add(data);
        }
        return dataList;
    }
}
