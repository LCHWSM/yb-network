package com.ybau.transaction.util;

import com.ybau.transaction.domain.ExcelProduct;
import com.ybau.transaction.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class RepetitionUtil {


    /**
     * 校验商品名字或编号是否重复
     *
     * @param name
     * @param core
     * @return
     */
    public static boolean checkForDuplicates(String name, String core, List<Product> products) {
        // 姓名与手机号相等个数不等于0则为重复
        return products.stream().anyMatch(e -> e.getName().equals(name) || e.getCore().equals(core));
    }

    /**
     * 判断导入数据中是否有重复行
     *
     * @param excelProducts
     * @return
     */
    public static int ifNameByCore(List<ExcelProduct> excelProducts) {
        int nameCount = excelProducts.stream()
                .collect(Collectors.groupingBy(a -> a.getName(), Collectors.counting()))
                .entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
                .collect(Collectors.toList()).size();
        int coreCount = excelProducts.stream()
                .collect(Collectors.groupingBy(a -> a.getCore(), Collectors.counting()))
                .entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
                .collect(Collectors.toList()).size();
        return nameCount+coreCount;
    }
}
