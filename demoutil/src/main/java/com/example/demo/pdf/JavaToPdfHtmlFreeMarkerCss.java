package com.example.demo.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import com.lowagie.text.DocumentException;
import freemarker.template.TemplateException;
import lombok.Cleanup;
import org.apache.commons.codec.binary.Base64;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 *根据 html模版生成pdf
 * @author fuzi
 */
public class JavaToPdfHtmlFreeMarkerCss {
    /**生成路径*/
    private static final String DEST = "/Users/fuzi/workspace/doc/pdf/templateFCss.pdf";
    /**模版*/
    private static final String HTML = "templateFCss.html";
    /**字体*/
    private static final String FONT = "/Users/fuzi/workspace/doc/pdf/simhei.ttf";
    /**图*/
    private static final String LOGO_PATH = "/Users/fuzi/workspace/doc/pdf/111.png";

    private static Configuration freemarkerCfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    public static void main(String[] args) throws IOException, DocumentException, TemplateException {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("name","路奇.D.艾尼路 儸傑");
        data.put("fileType","image/jpeg");
        data.put("file64Str",fileToBase64Str());
        String content =freeMarkerRender(data);
        createPdf(content);

    }

    /**
     * freemarker渲染html ，填充数据
     *
     * @param data
     * @return
     */
    public static String freeMarkerRender(Map<String, Object> data) throws IOException, TemplateException {
        @Cleanup Writer out = new StringWriter();
        // 获取模板,并设置编码方式
        Template template = freemarkerCfg.getTemplate(HTML,"UTF-8");
        // 合并数据模型与模板 //将合并后的数据和模板写入到流中，这里使用的字符流
        template.process(data, out);
        out.flush();
        return out.toString();
    }

    /**
     * 输出pdf
     * @param content
     * @throws IOException
     * @throws DocumentException
     */
    public static void createPdf(String content) throws IOException, DocumentException {
        ITextRenderer render = new ITextRenderer();
        //设置字体
        ITextFontResolver fontResolver = render.getFontResolver();
        fontResolver.addFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        // 解析html生成pdf
        render.setDocumentFromString(content);
        render.layout();
        render.createPDF(new FileOutputStream(DEST));
        render.finishPDF();
    }


    /**
     * File to 64bit Str
     *
     * @param
     * @return
     */
    public static String fileToBase64Str() throws IOException{
        File file = new File(LOGO_PATH);
        @Cleanup InputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);
        return Base64.encodeBase64String(data);
    }
}
