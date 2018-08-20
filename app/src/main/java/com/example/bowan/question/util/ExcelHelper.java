package com.example.bowan.question.util;



import android.telecom.Call;
import android.util.Log;

import com.aspose.cells.CalculationOptions;
import com.aspose.cells.Cells;
import com.aspose.cells.License;
import com.aspose.cells.Picture;
import com.aspose.cells.PictureCollection;
import com.aspose.cells.Worksheet;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Detail;
import com.example.bowan.question.entity.Summary;

import junit.framework.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelHelper {
    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Test.class.getClassLoader().getResourceAsStream("\\license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int writeLostInfo(Worksheet sheet, int line, HashMap<String, String> hashMap, String imagePaths) {
        try {
            final int BG_WIDTH = 392;
            final int SM_WIDTH = 252;
            final int BG_HEIGHT = 292;
            final int SM_HEIGHT = 189;
            ArrayList<Integer> indexImage = new ArrayList<Integer>();
            Cells cells = sheet.getCells();

            cells.get(line, 0).putValue(hashMap.get("chapter"));
            cells.get(line, 1).putValue(hashMap.get("item"));
            cells.get(line, 2).putValue(hashMap.get("description"));
            cells.get(line, 3).putValue(hashMap.get("value"));
            cells.get(line, 4).putValue(hashMap.get("score"));
            cells.get(line, 5).putValue(hashMap.get("error"));
            sheet.autoFitRow(line);
            double textLineHeight = cells.getRowHeightPixel(line);

            double ContentHeightLb;
            double CmLbRate = 28.3465;
            if (line == 2) {
                ContentHeightLb = 453;
            } else {
                ContentHeightLb = 514;
            }

            double height = (ContentHeightLb - textLineHeight - 15) / 2;
            cells.setRowHeight(line + 1, height);
            cells.setRowHeight(line + 2, height);

            PictureCollection pics = sheet.getPictures();
            Picture p;

            if (imagePaths == null || imagePaths.isEmpty()) {
                return line;
            }
            String[] images = imagePaths.split("\\|");
            int picCount = images.length;

            if (picCount > 3 && textLineHeight > 70) {
                //分两页，small
                StringBuffer p1 = new StringBuffer();
                StringBuffer p2 = new StringBuffer();

                for (int i = 0; i < picCount; i++) {
                    if (i < 3) {
                        p1.append("|"+images[i]);
                    } else {
                        p2.append("|"+images[i]);
                    }
                }

                writeLostInfo(sheet, line, hashMap, p1.toString().substring(1));
                return writeLostInfo(sheet, line + 3, hashMap, p2.toString().substring(1));
            } else {
                for (String img : images) {
//                    File f = new File("a.tmp");
//                    FileOutputStream fos = new FileOutputStream(f);
//                    Thumbnails.of(img).size(BG_WIDTH, BG_HEIGHT).outputQuality(0.5).toOutputStream(fos);
//                    fos.close();
//                    FileInputStream fis = new FileInputStream(f);
                    int i = pics.add(line + 1, 0, img);
//                    fis.close();
                    indexImage.add(i);
                }
                if (picCount == 2 && textLineHeight < 150) {
                    //big,392
                    for (int i = 0; i < picCount; i++) {
                        int index = indexImage.get(i);
                        p = pics.get(index);
                        p.setTop(10);
                        if (i == 0) {
                            p.setX(10);
                        } else {
                            p.setX(10 + BG_WIDTH + 10);
                        }
                        p.setWidth(BG_WIDTH);
                        p.setHeight(BG_HEIGHT);
                    }
                    return line;
                }


                for (int i = 0; i < picCount; i++) {
                    int index = indexImage.get(i);
                    p = pics.get(index);
                    p.setWidth(SM_WIDTH);
                    p.setHeight(SM_HEIGHT);

                    if (i < 3) {
                        p.setTop(10);
                    } else {
                        p.setTop(SM_HEIGHT + 10);
                    }
                    if (i == 0 || i == 3) {
                        p.setX(10);
                    } else if (i == 1 || i == 4) {
                        p.setX(20 + SM_WIDTH);
                    } else if (i == 2 || i == 5) {
                        p.setX(30 + 2 * SM_WIDTH);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return line;

    }

    public static void writeCover(Worksheet sheet, HashMap<String, String> data) {
        try {
            Cells cells = sheet.getCells();
            cells.get("D16").putValue(data.get("code"));
            cells.get("I16").putValue(data.get("fullName"));
            cells.get("D17").putValue(data.get("area"));
            cells.get("I17").putValue(data.get("city"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeSummary(Worksheet sheet, List<Summary> summaries) {
        try {
            Cells cells = sheet.getCells();
            int maxRow = cells.getMaxDataRow();
            for (int i = 4; i < maxRow; i++) {
                Object o = cells.get("C"+i).getValue();
                if (o != null) {
                    String a = o.toString().trim();
                    for (Summary summary : summaries) {
                        if (summary.getCcid().equals(a)) {
                            cells.get("E"+i).putValue(summary.getSumTB());
                            cells.get("F"+i).putValue(summary.getSumTE());
                            cells.get("G"+i).putValue(summary.getSumSB());
                            cells.get("H"+i).putValue(summary.getSumSE());
                            break;
                        }
                    }
                }
            }
            CalculationOptions op = new CalculationOptions();
            op.setIgnoreError(true);
            sheet.calculateFormula(op,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeDetail(Worksheet sheet, List<Detail> details) {
        try {
            Cells cells = sheet.getCells();
            int maxRow = cells.getMaxDataRow();
            for (int i = 3; i < maxRow; i++) {
                String a = cells.get("B"+i).getValue().toString().trim();
                for (Detail detail : details) {
                    if (detail.getCid().equals(a)) {
                        cells.get("F"+i).putValue(detail.getMaxSB());
                        cells.get("G"+i).putValue(detail.getMaxSE());
                        cells.get("H"+i).putValue(detail.getMaxPoints()==null?"":detail.getMaxPoints());
                        cells.get("I"+i).putValue((detail.getMaxTips())==null?"":detail.getMaxTips());
                        break;
                    }
                }
            }
            CalculationOptions op = new CalculationOptions();
            op.setIgnoreError(true);
            sheet.calculateFormula(op,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<HashMap<String,String>> getDefectImageRows(Worksheet scoreDetails, List<Map<String, String>> imageItems) {
        Cells cells = scoreDetails.getCells();
        int maxRow = cells.getMaxDataRow();
        List<HashMap<String, String>> rows = new ArrayList<>();
        for (int i=3; i<maxRow; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            String cid = cells.get("B"+i).getValue().toString();
            hashMap.put("chapter", cid);
            hashMap.put("item", cells.get("C"+i).getValue().toString());
            hashMap.put("description", cells.get("E"+i).getValue().toString());
            String value = cells.get("J"+i).getValue()==null?"":cells.get("J"+i).getValue().toString();
            hashMap.put("value",value);
            String sorce = cells.get("H"+i).getValue()==null?"":cells.get("H"+i).getValue().toString();
            hashMap.put("score", sorce);
            String error = cells.get("I"+i).getValue()==null?"":cells.get("I"+i).getValue().toString();
            hashMap.put("error", error);
            String imagePaths = getImagePathByCid(cid, imageItems);

            hashMap.put("imgs", imagePaths);
            rows.add(hashMap);
        }
        return rows;
    }

    private static String getImagePathByCid(String cid, List<Map<String, String>> imageItems) {
        for (Map<String, String> imageItem : imageItems) {
            if (imageItem.get("cid").equals(cid)) {
                return imageItem.get("images");
            }
        }
        return null;
    }

}
