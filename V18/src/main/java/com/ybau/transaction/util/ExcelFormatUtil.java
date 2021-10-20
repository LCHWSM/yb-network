package com.ybau.transaction.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelFormatUtil {


    /**
     * 设置报表头样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle headSytle(SXSSFWorkbook workbook) {
        // 设置style1的样式，此样式运用在第二行
        CellStyle style1 = workbook.createCellStyle();// cell样式
        // 设置单元格背景色，设置单元格背景色以下两句必须同时设置
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置填充样式
        style1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置填充色
        // 设置单元格上、下、左、右的边框线
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        Font font1 = workbook.createFont();// 创建一个字体对象
        font1.setBoldweight((short) 10);// 设置字体的宽度
        font1.setFontHeightInPoints((short) 10);// 设置字体的高度
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style1.setFont(font1);// 设置style1的字体
        style1.setWrapText(true);// 设置自动换行
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置单元格字体显示居中（左右方向）
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 设置单元格字体显示居中(上下方向)
        return style1;
    }

    /**
     * 设置报表体样式
     *
     * @param wb
     * @return
     */
    public static CellStyle contentStyle(SXSSFWorkbook wb) {
        // 设置style1的样式，此样式运用在第二行
        CellStyle style1 = wb.createCellStyle();// cell样式
        // 设置单元格上、下、左、右的边框线
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setWrapText(true);// 设置自动换行
        style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 设置单元格字体显示居中（左右方向）
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 设置单元格字体显示居中(上下方向)
        return style1;
    }

    /**
     * 设置报表标题样式
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle titleSytle(HSSFWorkbook workbook, short color, short fontSize) {
        // 设置style1的样式，此样式运用在第二行
        HSSFCellStyle style1 = workbook.createCellStyle();// cell样式
        // 设置单元格背景色，设置单元格背景色以下两句必须同时设置
        //style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置填充样式
        //short fcolor = color;
        if (color != HSSFColor.WHITE.index) {
            style1.setFillForegroundColor(color);// 设置填充色
        }
        // 设置单元格上、下、左、右的边框线
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        HSSFFont font1 = workbook.createFont();// 创建一个字体对象
        font1.setBoldweight(fontSize);// 设置字体的宽度
        font1.setFontHeightInPoints(fontSize);// 设置字体的高度
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        style1.setFont(font1);// 设置style1的字体
        style1.setWrapText(true);// 设置自动换行
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置单元格字体显示居中（左右方向）
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 设置单元格字体显示居中(上下方向)
        return style1;
    }

    /**
     * 设置表头
     *
     * @param sheet
     */
    public static void initTitleEX(SXSSFSheet sheet, CellStyle header, String title[], int titleLength[]) {
        SXSSFRow row0 = (SXSSFRow) sheet.createRow(0);
        row0.setHeight((short) 800);
        for (int j = 0; j < title.length; j++) {
            SXSSFCell cell = (SXSSFCell) row0.createCell(j);
            //设置每一列的字段名
            cell.setCellValue(title[j]);
            cell.setCellStyle(header);
            sheet.setColumnWidth(j, titleLength[j]);
        }
    }

    public static int getVaildRows(Sheet sheet)  {
        CellReference cellReference = new CellReference("A4");
        boolean flag = false;
        for (int i = 9; i <= sheet.getLastRowNum(); ) {
            Row r = sheet.getRow(i);
            if (r == null) {
                // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                continue;
            }
            flag = false;
            for (Cell c : r) {
                if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i++;
                continue;
            } else {//如果是空白行（即可能没有数据，但是有一定格式）
                if (i == sheet.getLastRowNum())//如果到了最后一行，直接将那一行remove掉
                    sheet.removeRow(r);
                else//如果还没到最后一行，则数据往上移一行
                    sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
            }
        }
        return sheet.getLastRowNum() + 1;
    }
}