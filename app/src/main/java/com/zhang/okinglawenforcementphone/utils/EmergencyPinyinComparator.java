package com.zhang.okinglawenforcementphone.utils;

import com.zhang.okinglawenforcementphone.beans.GreenMember;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;


/**
 * Created by Administrator on 2018/1/5.
 */

public class EmergencyPinyinComparator implements Comparator<GreenMember> {


    @Override
    public int compare(GreenMember t0, GreenMember t1) {

        String t1Username = t0.getUsername();
        if (t1Username.startsWith("曾")){
            t1Username= t1Username.replace("曾","增");
        }
        String t2Username = t1.getUsername();
        if (t2Username.startsWith("曾")){
            t2Username=t2Username.replace("曾","增");
        }

        return concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(t1Username.charAt(0)))
                .compareTo(concatPinyinStringArray(PinyinHelper
                        .toHanyuPinyinStringArray(t2Username.charAt(0))));

    }

    private String concatPinyinStringArray(String[] pinyinArray) {
        StringBuffer pinyinSbf = new StringBuffer();
        if ((pinyinArray != null) && (pinyinArray.length > 0)) {
            for (int i = 0; i < pinyinArray.length; i++) {
                pinyinSbf.append(pinyinArray[i]);
            }
        }
        return pinyinSbf.toString();
    }
}
