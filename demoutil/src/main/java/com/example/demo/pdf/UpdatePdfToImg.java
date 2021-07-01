package com.example.demo.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class UpdatePdfToImg {
    private static final String DEST = "/Users/fuzi/github/tools/doc/pdf/recipe.pdf";
    private static final String DSTIMGFOLDER = "/Users/fuzi/Downloads";

    public static void main(String[] args) {
        pdf2Image(150);
    }

    /***
     * PDF文件转PNG图片，全部页数
     *
     * @param PdfFilePath pdf完整路径
     * @param imgFilePath 图片存放的文件夹
     * @param dpi dpi越大转换后越清晰，相对转换速度越慢
     * @return
     */
    public static void pdf2Image(int dpi) {
        try {
            PDDocument pdDocument = PDDocument.load(new File(DEST));
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            BufferedImage image = renderer.renderImageWithDPI(0, dpi);
            // 获取图片文件名
            File dstFile = new File(DSTIMGFOLDER + File.separator + "recipe2.png");
            ImageIO.write(image, "png", dstFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
