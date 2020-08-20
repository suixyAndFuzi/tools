package com.example.demo.pdf;
import java.io.*;
import java.nio.charset.StandardCharsets;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.Cleanup;
import org.apache.commons.codec.binary.Base64;

/**
 * html转pdf
 *
 * @author zhangwd
 * @version 1.0
 *
 */
public class htmlToPdf {

    public static void create() throws Exception {
        String html ="<!DOCTYPE html> <html> <head> <meta charset=\"UTF-8\"/> <title>Title</title> <style> body{ font-family:SimHei; } .color{ color: green; } .pos{ position:absolute; left:200px; top:5px; width: 200px; font-size: 10px; } @media print { div.header-right { display: block; position: running(header-right); } } @page { size: 8.5in 11in; @top-right { content: element(header-right) }; /*@bottom-center { content : \"Page \" counter(page) \" of \" counter(pages); };\t */ @bottom-center { content: element(footer) } } #footer { position: running(footer); } #pages:before { content: counter(page); } #pages:after { content: counter(pages); } </style> </head> <body style='font-size:20px;font-family:SimSun;'> <div id=\"footer\"> <div style=\"text-align: center; width: 100%;font-size: 15px;\">Page <span id=\"pages\"> of </span></div> </div> <div class=\"page\"> <div class=\"color\">你好，${name}222</div> <img src=\"data:${fileType};base64,${file64Str}\" width=\"600px\" /> </div> </body> </html>";
        html =  html.replace("${name}","xxxxxxx").replace("${file64Str}",fileToBase64Str()).replace("${fileType}","image/jpeg");
        InputStream is = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        OutputStream os = new FileOutputStream("/Users/fuzi/workspace/demo.4.pdf");
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document,os);

        document.open();
        String fontPath="/Users/fuzi/workspace/doc/pdf/simhei.ttf";
        // 将html转pdf
        XMLWorkerHelper.getInstance().parseXHtml(writer,document, is, StandardCharsets.UTF_8,new AsianFontProvider());

        document.close();
    }

    public static void main(String[]args) throws Exception {
        create();
    }
    private static final String LOGO_PATH = "/Users/fuzi/workspace/doc/pdf/111.png";
    public static String fileToBase64Str() throws IOException {
        File file = new File(LOGO_PATH);
        @Cleanup InputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);
        return Base64.encodeBase64String(data);
    }
}