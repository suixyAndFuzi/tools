package com.example.demo.pdf;

import  com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Getter
@Setter
class WordToPdfBean{
    private String key;
    private String value;
    private int type;
    public WordToPdfBean(String key,String value,int type){

        this.key = key;
        this.value = value;
        this.type = type;
    }
}

/**
 * word 方式生成pdf模版
 * @author fuzi
 */
public class WordToPdf {
    /**生成的新文件路径**/
    private static final String NEW_PDF_PATH = "/Users/fuzi/workspace/doc/pdf/testNew.pdf";
    /**模板路径*/
    private static final String TEMPLATE_PATH = "/Users/fuzi/workspace/doc/pdf/test.pdf";

    private static final String IMG = "/Users/fuzi/workspace/doc/pdf/111.png";

    public static void main(String[] args) {
        WordToPdfBean w = new  WordToPdfBean("name","张三",1);
        WordToPdfBean w2 = new  WordToPdfBean("age","123",1);
        WordToPdfBean w3 = new  WordToPdfBean("xxx",IMG,2);
        WordToPdfBean w4 = new  WordToPdfBean("yyy",IMG,2);
        List<WordToPdfBean> list = Arrays.asList(w,w2,w3,w4);
        try {
            pdfOut(list);
        } catch (IOException | DocumentException e) {
            System.out.println(e);
        }
    }

    /**
     * pdf 读取输出流 方法
     * @param list
     */
    public static void pdfOut(List<WordToPdfBean> list) throws IOException, DocumentException{
            // 读取pdf模板
            PdfReader reader = new PdfReader(TEMPLATE_PATH);
            @Cleanup  ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
            //组织参数
            pdf(list,form);
            // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.setFormFlattening(true);
            stamper.close();

            // 输出流
            @Cleanup FileOutputStream out = new FileOutputStream(NEW_PDF_PATH);
            Document doc = new Document();
            PdfSmartCopy copy = new PdfSmartCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
    }

    /**
     * 模版填充 数据方法
     * @param list
     * @param form
     */
    private static void pdf(List<WordToPdfBean> list, AcroFields form) throws IOException, DocumentException{
        for (WordToPdfBean a:list) {
            if(1==a.getType()){
                //文字类的内容处理
                form.setField(a.getKey(),a.getValue());
            }else {
                //将图片写入指定的field
                String key = a.getKey();
                Image image = Image.getInstance(a.getValue());
                PushbuttonField pb = form.getNewPushbuttonFromField(key);
                pb.setImage(image);
                form.replacePushbuttonField(key, pb.getField());
            }
        }
    }
}
