package com.aiyuns.tinkerplay.Utils;

import com.aiyuns.tinkerplay.Config.PdfConfig;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/** 根据pdf模板生成pdf文件生成到某路径下或导出 */
@Component
public class PdfUtil {

  // pdf配置类对象
  @Resource(name = "PdfConfig")
  private PdfConfig config;

  private static PdfConfig pdfConfig;

  @PostConstruct
  public void init() {
    pdfConfig = config;
  }

  /** 利用模板生成pdf保存到某路径下 */
  public static String pdfOut(Map<String, Object> inputMap) {

    // 生成的新文件路径
    String path0 = pdfConfig.getOutput();
    File f = new File(path0);

    if (!f.exists()) {
      f.mkdirs();
    }
    // 模板路径
    String templatePath = pdfConfig.getTemplatePath();
    // 创建文件夹
    String newPdfPath = path0;
    File file1 = new File(newPdfPath);
    if (!file1.exists()) {
      try {
        file1.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    PdfReader reader;
    FileOutputStream out;
    ByteArrayOutputStream bos;
    PdfStamper stamper;
    try {
      BaseFont bf = BaseFont.createFont();
      // 输出流
      String outPath = newPdfPath + System.currentTimeMillis() + ".pdf";
      out = new FileOutputStream(outPath);
      // 读取pdf模板
      ClassPathResource resource = new ClassPathResource(templatePath);
      InputStream inputStream = resource.getInputStream();
      reader = new PdfReader(inputStream);
      bos = new ByteArrayOutputStream();
      stamper = new PdfStamper(reader, bos);
      AcroFields form = stamper.getAcroFields();
      // 文字类的内容处理
      Map<String, String> datemap = (Map<String, String>) inputMap.get("user");
      form.addSubstitutionFont(bf);
      for (String key : datemap.keySet()) {
        String value = datemap.get(key);
        form.setField(key, value);
      }
      stamper.setFormFlattening(false);
      stamper.close();
      Document doc = new Document();
      PdfCopy copy = new PdfCopy(doc, out);
      doc.open();
      PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
      copy.addPage(importPage);
      doc.close();
      return outPath;
    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    }
    return "";
  }

  /** 利用模板生成pdf导出 */
  public static void pdfExport(Map<String, Object> inputMap, HttpServletResponse response) {

    // 生成的新文件路径
    String path0 = pdfConfig.getOutput();
    File f = new File(path0);

    if (!f.exists()) {
      f.mkdirs();
    }
    // 模板路径
    String templatePath = pdfConfig.getTemplatePath();

    File file = new File(templatePath);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    PdfReader reader;
    ByteArrayOutputStream bos;
    PdfStamper stamper;
    OutputStream out = null;
    try {
      Map<String, String> datemap = (Map<String, String>) inputMap.get("dateMap");
      BaseFont bf =
          BaseFont.createFont(pdfConfig.getFontPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
      // 输出流
      response.setContentType("application/pdf");
      response.setHeader(
          "Content-Disposition",
          "attachment;fileName=" + URLEncoder.encode(datemap.get("billId") + ".pdf", "UTF-8"));
      out = new BufferedOutputStream(response.getOutputStream());
      // 读取pdf模板
      reader = new PdfReader(templatePath);
      bos = new ByteArrayOutputStream();
      stamper = new PdfStamper(reader, bos);
      AcroFields form = stamper.getAcroFields();
      // 文字类的内容处理
      form.addSubstitutionFont(bf);
      for (String key : datemap.keySet()) {
        String value = datemap.get(key);
        form.setField(key, value);
      }
      stamper.setFormFlattening(false);
      stamper.close();
      Document doc = new Document();
      PdfCopy copy = new PdfCopy(doc, out);
      doc.open();
      PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
      copy.addPage(importPage);
      doc.close();

    } catch (IOException | DocumentException e) {
      System.out.println(e);
    } finally {
      try {
        assert out != null;
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
