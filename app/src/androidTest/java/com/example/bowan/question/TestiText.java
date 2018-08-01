package com.example.bowan.question;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestiText {
    public static void main(String[] args) throws IOException {
        File file = new File("D://test.pdf");
        // 实例化一个可以写pdf文件的对象, 参数可以是File,流,文件名等


        PdfWriter writer=new PdfWriter(file);
        // 得到一个Pdf的Document
        PdfDocument pdf=new PdfDocument(writer);
        // 实例化一个Document, 此实例不只可以写pdfDocument, 传入pdfDocument, 指定使其生成pdfDocument
        Document document=new Document(pdf,PageSize.A4.rotate());
        // 设置字体, 使其支持中文
        PdfFont sysFont = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        //PdfFont diyFont = PdfFontFactory.createFont("D://workspace//test//simsum.ttf,1", PdfEncodings.IDENTITY_H,true);  自己指定字体文件


        // 实例化一个段落, 这是写pdf文件的基本单位
        Paragraph para = new Paragraph("Hello Worlddafad, 你好,中国");
        // 设置段落的字体
        para.setFont(sysFont);
        // 写入一个图片
        Image img = new Image(ImageDataFactory.create("D://正则表达式.png"));
        // 将段落和图片都加入document
        document.add(para);
        document.add(img);
        // 关闭document对象
        document.close();
    }
}
