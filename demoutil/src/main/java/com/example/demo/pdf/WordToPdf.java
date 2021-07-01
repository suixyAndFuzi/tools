package com.example.demo.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Arrays;
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
 *
 * @author fuzi
 */
public class WordToPdf {
    /**
     * 生成的新文件路径
     **/
    //private static final String NEW_PDF_PATH = "/Users/fuzi/workspace/doc/pdf/recipeDrug.pdf";
    private static final String DEST = "/Users/fuzi/Downloads/add.pdf";
    private static final String DEST1 = "/Users/fuzi/Downloads/dddd.pdf";
    /**
     * 模板路径
     */
    //private static final String TEMPLATE_PATH = "/Users/fuzi/github/tools/doc/pdf/pdfmb.pdf";

    private static final String TEMPLATE_PATH = "/Users/fuzi/github/tools/pdfmb.pdf";

    private static final String IMG = "/Users/fuzi/github/tools/doc/pdf/111.png";

    public static void main(String[] args) {

        WordToPdfBean w = new WordToPdfBean("recipe.organName", "福建省立医院互联网医院", 1);
        WordToPdfBean w2 = new WordToPdfBean("visitIdOutter", "3831291", 1);
        WordToPdfBean w3 = new WordToPdfBean("recipe.recipeId", "ngri12", 1);
        WordToPdfBean w16 = new WordToPdfBean("recipeCode1", "ngri121345689", 1);
        WordToPdfBean w4 = new WordToPdfBean("patient.patientName", "昱平", 1);
        WordToPdfBean w5 = new WordToPdfBean("patient.patientSex", "男", 1);
        WordToPdfBean w6 = new WordToPdfBean("pAge", "123", 1);

        WordToPdfBean w7 = new WordToPdfBean("recipeExtend.cardNo", "自费", 1);
        WordToPdfBean w8 = new WordToPdfBean("recipe.departText", "时间科", 1);
        WordToPdfBean w9 = new WordToPdfBean("recipe.signDate", "2008-12-08 13:62", 1);
        WordToPdfBean w10 = new WordToPdfBean("patient.mobile", "13697238800", 1);
        WordToPdfBean w11 = new WordToPdfBean("recipe.organDiseaseName", "的环境的看哈客户端画口红的哈哈哈看见好看好看花见花开卡号很快就和空间水水水水还是", 1);
        WordToPdfBean w12 = new WordToPdfBean("myTitle", "222222222", 1);
//        WordToPdfBean w3 = new  WordToPdfBean("xxx",IMG,2);
        WordToPdfBean w13 = new WordToPdfBean("recipeExtend.superviseRecipecode", IMG, 2);

        WordToPdfBean w14 = new WordToPdfBean("recipeType", "普通", 1);
        WordToPdfBean w15 = new WordToPdfBean("weight", "45", 1);

        WordToPdfBean w18 = new WordToPdfBean("doctorSignImg,doctorSignImgToken", "昱平", 1);

        List<WordToPdfBean> list = Arrays.asList(w, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14, w15, w16, w18);
        try {
            pdfOut(list);
        } catch (IOException | DocumentException e) {
            System.out.println(e);
        }

        generateCoOrdinatePdf();
    }


    /**
     * pdf 读取输出流 方法
     *
     * @param list
     */
    public static void pdfOut(List<WordToPdfBean> list) throws IOException, DocumentException {
        // 读取pdf模板
        PdfReader reader = new PdfReader(TEMPLATE_PATH);
        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
        //组织参数
        pdf(list, form);
        // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
        stamper.setFormFlattening(true);
        stamper.close();

        // 输出流
        @Cleanup FileOutputStream out = new FileOutputStream(DEST);
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
            if (1 == a.getType()) {
                //文字类的内容处理
                form.setField(a.getKey(), a.getValue());
            } else {
                //将图片写入指定的field
                String key = a.getKey();
                Image image = Image.getInstance(a.getValue());
                PushbuttonField pb = form.getNewPushbuttonFromField(key);
                pb.setImage(image);
                form.replacePushbuttonField(key, pb.getField());
            }
        }

        getPdfTextPosition(form);
    }

    /**
     * 定位某个表单字段坐标 x,y
     *
     * @param fields
     */
    public static void getPdfTextPosition(AcroFields fields) {
        List<AcroFields.FieldPosition> pos = fields.getFieldPositions("checkerSignImg,checkerSignImgToken");
        AcroFields.FieldPosition pitem = pos.get(0);
        Rectangle pRectangle = pitem.position;
        String res = pRectangle.getLeft() + "," + pRectangle.getBottom();
        System.out.println(res);
    }

    /**
     * 根据坐标 x，y 填充文字
     */
    public static void generateCoOrdinatePdf() {
        try {
            PdfReader reader = new PdfReader(DEST);
            @Cleanup OutputStream output = new FileOutputStream(new File(DEST1));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            //添加文本块
            page.beginText();
            page.setColorFill(BaseColor.BLACK);
            page.setFontAndSize(bf, 10);
            page.setTextMatrix(301.605F, 270.123F);
            page.showText("decoction.getValue()谢谢谢谢谢谢谢谢谢谢谢谢谢谢谢谢谢谢");
            page.endText();
            stamper.close();
            reader.close();
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
    }
}
