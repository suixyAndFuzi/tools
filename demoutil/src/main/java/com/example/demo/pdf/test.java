package com.example.demo.pdf;
import com.itextpdf.forms.PdfAcroForm;

import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


public class test {


    public static final String DEST = "/Users/fuzi/Downloads/add.pdf";

    public static void main(String[] args) throws Exception {
        new test().manipulatePdf();
    }

//按钮
//    protected void manipulatePdf() throws Exception {
//        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
//        PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
//        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
//        //此坐标，长宽
//        Rectangle rect = new Rectangle(100, 100, 108, 26);
//        PdfButtonFormField pushButton = PdfFormField.createPushButton(pdfDoc, rect, "japanese", "大家好", font, 12f);
//        form.addField(pushButton);
//
//        pdfDoc.close();
//    }

    protected void manipulatePdf() throws Exception {
        //生产文件到固定位置
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        //设置 pdf纸张
        Document doc = new Document(pdfDoc, PageSize.A4);
        //设置中文字体
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        //创建表单对象
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.setNeedAppearances(true);
        //设置表单text类型数据 name为key 后期可根据动态替换 multiline是自动折行设置
        PdfTextFormField field1 = PdfFormField.createText(pdfDoc, new Rectangle(20, 788, 50, 18),
                "myfile1", "你好:",font,PdfFormField.DEFAULT_FONT_SIZE);
        PdfTextFormField field = PdfFormField.createText(pdfDoc, new Rectangle(120, 788, 100, 18),
                "myfile", "3333",font,PdfFormField.DEFAULT_FONT_SIZE);
        form.addField(field1);

        form.addField(field);

        PdfTextFormField title = PdfFormField.createText(pdfDoc, new Rectangle(36, 752, 100, 30),
                "mytitle", "111111111111111",null,PdfFormField.DEFAULT_FONT_SIZE,true);
        form.addField(title);

        //表单不可变编辑设置--默认可编辑
        form.flattenFields();

        //编辑文字 setRelativePosition方法为动态左边 左上右下
        doc.add(new Paragraph("Hello World").setRelativePosition(100,40,100,50));

        //画线
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.setStrokeColor(DeviceCmyk.BLACK).moveTo(36, 752).lineTo(600, 752).closePathStroke();
        canvas.setStrokeColor(DeviceCmyk.BLACK).moveTo(100, 500).lineTo(30, 300).closePathStroke();

        //关闭
        pdfDoc.close();
    }

}
