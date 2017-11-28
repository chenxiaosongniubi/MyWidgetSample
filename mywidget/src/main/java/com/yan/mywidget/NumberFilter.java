package com.yan.mywidget;

import android.text.InputFilter;
import android.text.Spanned;

import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * package:com.dodoca.dodopay.widget
 * author:yanweiqiang
 * since:2016/1/27 15:07
 * description:DodoPay
 */
public class NumberFilter implements InputFilter {
    private final String tag = NumberFilter.class.getSimpleName();

    private double maxValue = Double.MAX_VALUE;
    private double pointLen = Double.MAX_VALUE;
    private Pattern oneNumber;
    private Pattern legalNumber;

    public NumberFilter() {
        oneNumber = Pattern.compile("^[\\d]+$");
        legalNumber = Pattern.compile("^[\\d]+[.]?[\\d]*$");
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setPointLen(int pointLen) {
        this.pointLen = pointLen;
    }

    /**
     * source    新输入的字符串
     * start    新输入的字符串起始下标，一般为0
     * end    新输入的字符串终点下标，一般为source长度-1
     * dest    输入之前文本框内容
     * dstart    原内容起始坐标，一般为0
     * dend    原内容终点坐标，一般为dest长度-1
     */
    @Override
    public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
        String ret = getString(src, start, end, dest, dstart, dend);
        //Log.i(tag, src + "," + start + "," + end + "||" + dest + "," + dstart + "," + dend);
        //Log.i(tag, ret);

        //验证删除等按键
        if ("".equals(src.toString())) {
            if (ret.length() > 0) {
                if (!legalNumber.matcher(ret).matches()) {
                    return dest.subSequence(dstart, dend);
                }
                if (Double.parseDouble(ret) > maxValue) {
                    return dest.subSequence(dstart, dend);
                }
            }
            return null;
        }

        //验证小数点
        if (".".equals(src.toString())) {
            if (!legalNumber.matcher(ret).matches()) {
                return dest.subSequence(dstart, dend);
            }
            return src.toString();
        }

        //验证数字
        if (!oneNumber.matcher(src).matches()) {
            return "";
        }

        if (!legalNumber.matcher(ret).matches()) {
            return dest.subSequence(dstart, dend);
        }

        double number = Double.parseDouble(ret);

        //Log.i(tag, "number:" + number);

        //验证数字大小
        if (number > maxValue) {
            return dest.subSequence(dstart, dend);
        }

        //验证小数位精度是否正确
        if (ret.contains(".")) {
            int index = ret.indexOf(".");
            int len = ret.length() - index - 1;
            if (len > pointLen) {
                return "";
            }
        }

        return src.toString();
    }

    private String getString(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence sectionA = dest.subSequence(0, dstart);
        CharSequence sectionB = dest.subSequence(dend, dest.length());
        return MessageFormat.format("{0}{1}{2}", sectionA, src, sectionB);
    }
}
