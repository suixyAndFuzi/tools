package com.example.demo.ExistsUtil;

import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * 模板下载
 * Created by Administrator on 2018/5/10.
 */
public class file {

    public static final int ROW_WIDTH = 5;//列宽

    public static final String SHEET_NAME = "demo";//默认页名字

    private static Logger log = LoggerFactory.getLogger(file.class);

    /**
     * 判断文件为空 空生成
     *
     * @param path      生成路径
     * @param sheetName Excle首页名字
     * @param heads     首行模板格子数组
     */
    public static void fileIsExists(String path, String sheetName, String[] heads) {
        Workbook wb;
        @Cleanup OutputStream os;
        File file = new File(path);
        if (!file.exists()) {
            try {
                //生成文件
                wb = new HSSFWorkbook();
                os = new FileOutputStream(path);

                //创建页 Sheet对象
                Sheet sheet = wb.createSheet(sheetName);
                //设置格式excel 为文本格式
                CellStyle css = wb.createCellStyle();
                DataFormat format = wb.createDataFormat();
                css.setDataFormat(format.getFormat("@"));

                //创建首行模板格子
                if (null != heads && heads.length > 0) {
                    //创建首行 Row对象
                    Row row = sheet.createRow(0);
                    for (int i = 0; i < heads.length; i++) {
                        //创建格子 Cell对象 设置单元格的值
                        row.createCell(i).setCellValue(heads[i]);
                        //设置单元格 列宽 样式
                        sheet.setColumnWidth(i, (heads[i].length() + ROW_WIDTH) * 256);
                        sheet.setDefaultColumnStyle(i, css);
                    }
                }
                wb.write(os);
            } catch (Exception e) {
                log.error("生成xls文件出错  e== {}", e);
            }
        }
    }

    /**
     * 判断文件夹为空 空生成
     *
     * @param path
     */
    public static void folderIsExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 生成模板路径
     *
     * @return
     */
    public static String getModelPath(HttpServletRequest request, String productDir, String productModel, String modelCompare) {
        String path = request.getSession().getServletContext().getRealPath(productDir);
        folderIsExists(path);
        path = request.getSession().getServletContext().getRealPath(productDir + productModel);
        folderIsExists(path);
        path = path + modelCompare;
        return path;
    }

    /**
     * 获取二进制输入文件流
     *
     * @param path 文件路径
     * @param name 文件名
     * @return
     */
    public static final String CONTENT_TYPE = "application/msexcel;charset=utf-8";

    public static org.springframework.http.ResponseEntity<byte[]> getodelInputStream(String path, String name) {
        org.springframework.http.ResponseEntity<byte[]> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", name.replaceAll("/", ""));
        headers.setContentType(MediaType.valueOf(CONTENT_TYPE));
        try {
            @Cleanup InputStream inputStream = new FileInputStream(path);
            responseEntity = new org.springframework.http.ResponseEntity<byte[]>(IOUtils.toByteArray(inputStream), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("下载模板错误e==" + e);
            responseEntity = new org.springframework.http.ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    public static final String PRODUCT_DIR = "/product/";
    public static final String PRODUCT_MODEL = "/model/";
    public static final String MODEL_DICTIONARY_DATA = "/demo.xls";
    //调用入口
    ResponseEntity<byte[]> compareDataModel(HttpServletRequest request) {
        //后续参数可由前端提供 统一模板生成下载接口
        //获取文件路径
        String path = file.getModelPath(request,PRODUCT_DIR,PRODUCT_MODEL,MODEL_DICTIONARY_DATA);
        //判断文件是否空
        String[] heads = {"第一列", "第二列", "第三列", "第四列", "第五列"};
        file.fileIsExists(path,file.SHEET_NAME,heads);
        //获取输入文件流
        ResponseEntity<byte[]>	responseEntity = file.getodelInputStream(path,MODEL_DICTIONARY_DATA);
        return	responseEntity;
    }


}
