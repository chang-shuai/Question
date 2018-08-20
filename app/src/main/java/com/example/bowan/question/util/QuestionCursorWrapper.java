package com.example.bowan.question.util;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.bowan.question.entity.Detail;
import com.example.bowan.question.entity.GGGG;
import com.example.bowan.question.entity.Summary;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class QuestionCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public QuestionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Summary getSummery() {
        String ccid = getString(getColumnIndex("ccid"));
        int sumTB = getInt(getColumnIndex("sum_tb"));
        int sumTE = getInt(getColumnIndex("sum_te"));
        int sumSB = getInt(getColumnIndex("sum_sb"));
        int sumSE = getInt(getColumnIndex("sum_se"));

        Summary summery = new Summary();
        summery.setCcid(ccid);
        summery.setSumTB(sumTB);
        summery.setSumTE(sumTE);
        summery.setSumSB(sumSB);
        summery.setSumSE(sumSE);
        return summery;
    }


    public GGGG getGGGG() {
        GGGG gggg = new GGGG();
        gggg.setQid(getInt(getColumnIndex("qid")));
        gggg.setBonuspoint(getInt(getColumnIndex("bonuspoint")));
        gggg.setCcid(getString(getColumnIndex("ccid")));
        gggg.setCid(getString(getColumnIndex("cid")));
        gggg.setGuibi(getInt(getColumnIndex("guibi")));
//        byte[] val = getBlob(getColumnIndex("images"));
//
//        try {
//            String images = new String(val, "utf-8");
//            gggg.setImages(images);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        gggg.setImages(getString(getColumnIndex("images")));
        gggg.setOid(getInt(getColumnIndex("oid")));
        gggg.setPaita(getInt(getColumnIndex("paita")));
        gggg.setPoints(getInt(getColumnIndex("points")));
        gggg.setScore(getString(getColumnIndex("score")));
        gggg.setScore_base(getString(getColumnIndex("score_base")));
        gggg.setScore_extra(getString(getColumnIndex("score_extra")));
        gggg.setTips(getString(getColumnIndex("tips")));
        gggg.setType_base(getString(getColumnIndex("type_base")));
        gggg.setType_extra(getString(getColumnIndex("type_extra")));

        gggg.save();
        return gggg;
    }

    public Detail getDetails() {
        Detail details = new Detail();
        details.setCid(getString(getColumnIndex("cid")));
        details.setMaxPoints(getString(getColumnIndex("max_points")));
        details.setMaxSB(getString(getColumnIndex("max_sb")));
        details.setMaxSE(getString(getColumnIndex("max_se")));
        details.setMaxTips(getString(getColumnIndex("max_tips")));
        return details;
    }

    public Map<String, String> getImage() {
        Map<String, String> map = new HashMap<>();
        map.put("cid", getString(getColumnIndex("cid")));
        map.put("images", getString(getColumnIndex("images")));
        return map;
    }


}
