package com.example.bowan.question.util;

import com.example.bowan.question.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratePdf {

    private File pdfFilePath;
    private String pdfFileName;
    private String fontFileName;

    public GeneratePdf(File pdfFilePath, String fontFileName) {
        this.pdfFilePath = pdfFilePath;
        this.fontFileName = fontFileName;

    }

    public String generate(String content) {
        String result = "";
        Document document = null;
        try {
            File file = new File(pdfFilePath, getPdfFileName());
            file.createNewFile();

            Font font = getFont();
            document = new Document(); // PageSize.A4.rotate()
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            Paragraph p = new Paragraph(content, font);
            document.add(p);
            result = "完成";
        } catch (IOException e) {
            e.printStackTrace();
            result = "生成pdf文件错误";
        } catch (DocumentException e) {
            e.printStackTrace();
            result = "pdfDocument异常";
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return result;
    }

    private Font getFont() {
        Font font = null;
        try {
            fontFileName += ",1";
            font = new Font(BaseFont.createFont(fontFileName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));//中文简体
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //font.setFamily("STSongStd-Light");
        //font.setFamily("Droid Sans");
        //font.setColor(BaseColor.BLUE);// 颜色
        //font.setSize(40);

        return font;
    }

    private String getPdfFileName() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pdfFileName = sdf.format(now);
        return pdfFileName + ".pdf";
    }


}
