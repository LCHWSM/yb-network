package com.ybau.transaction.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtli {


    public static String addDate(int number) {
        String createDate = "1900-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String temp = "";
        try {
            Date date = sdf.parse(createDate);
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            // cl.set(Calendar.DATE, day);
            cl.add(Calendar.DATE, number);
            temp = sdf.format(cl.getTime());
            return temp;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return temp;
    }

    public static String displayWithComma(String str) {
        StringBuilder sb = new StringBuilder();
        int index = str.lastIndexOf(".");
        String str1 = str.substring(0, index);// 取出小数点前面的字符串
        String str2 = str.substring(index);// 存放小数点后面的内容
        if (str2.length() < 3) {
            str2 = str2 + "0";
        }
        str1 = new StringBuilder(str1).reverse().toString();// 将字符串颠倒过来
        int size = (str1.length() % 3 == 0) ? (str1.length() / 3) : (str1
                .length() / 3 + 1);// 将字符串每三位分隔开
        for (int i = 0; i < size - 1; i++) {
            sb.append(str1.substring(i * 3, i * 3 + 3) + ",");
        }// 前n-1段都用逗号连接上
        str1 = sb.toString() + str1.substring((size - 1) * 3);//将前n-1段和第n段连接在一起
        str1 = new StringBuilder(str1).reverse().toString();
        return str1 + str2;

    }
}
