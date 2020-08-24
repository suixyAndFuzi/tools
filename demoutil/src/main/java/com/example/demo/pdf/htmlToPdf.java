package com.example.demo.pdf;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.Cleanup;

/**
 * html转pdf
 *
 * @author zhangwd
 * @version 1.0
 *
 */
public class htmlToPdf {
    private static final String LOGO_PATH = "/Users/fuzi/workspace/doc/pdf/111.png";

    public static void main(String[]args) throws Exception {
        String html ="<!DOCTYPE html> <html> <head> <title>Title</title> <style> body{ font-family:SimHei; } .color{ color: green; } .pos{ position:absolute; left:200px; top:5px; width: 200px; font-size: 10px; } @media print { div.header-right { display: block; position: running(header-right); } } @page { size: 8.5in 11in; @top-right { content: element(header-right) }; @bottom-center { content: element(footer) } } #footer { position: running(footer); } #pages:before { content: counter(page); } #pages:after { content: counter(pages); } </style> </head> <body> <div id=\"footer\"> <div style=\"text-align: center; width: 100%;font-size: 15px;\">Page <span id=\"pages\"> of </span></div> </div> <div class=\"page\"> <div class=\"color\">你好，${name1}</div> </div> <div class=\"page\"> <div class=\"color\">你好，${name2}</div> </div> <div class=\"page\"> <div class=\"color\">你好，${name}</div> <img src=\"${file64Str}\" width=\"100px\" /> </div> </body> </html>";

        Map<String,String> map = new HashMap<>();
        map.put("${file64Str}",LOGO_PATH);
        map.put("${name}","xxxxxxx");
        map.put("${name1}","sssssss");
        map.put("${name2}","yyyyyy");
        create(map,html);
    }

    public static void create(Map<String,String> map, String html) throws Exception {
        for (String key : map.keySet()) {
            html = html.replace(key,map.get(key));
        }
        @Cleanup  InputStream is = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        @Cleanup  OutputStream os = new FileOutputStream("/Users/fuzi/workspace/demo.pdf");
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document,os);
        document.open();
        // 将html转pdf
        XMLWorkerHelper.getInstance().parseXHtml(writer,document, is, StandardCharsets.UTF_8,new AsianFontProvider());
        document.close();
    }
}