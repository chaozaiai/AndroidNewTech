package com.techidea.library.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caozhentian on 2017/3/23.
 */

public class DateUtil {

    public final static String TOKEN_FORMAT = "yyyy.MM.dd.HH";
    public final static String SIGIN_FORMAT = "yyyy/MM/dd/HH/mm";
    public final static String FORMAT_YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";

    public final static String MESSAGE_FORMAT = "yy-MM-dd HH:mm:ss:SSS";

    public static final String getDateFormat(Date date, String dateFormat) {
        SimpleDateFormat timeFormater = new SimpleDateFormat(dateFormat);
        String str = timeFormater.format(date);
        return str;
    }

//    Date date = new Date(Long.parseLong(item.getMessage().getCreateTime()));

    public static String getDateFormat(Long millseconds, String dateFormat) {
        Date date = new Date(millseconds);
        SimpleDateFormat timeFormater = new SimpleDateFormat(dateFormat);
        String str = timeFormater.format(date);
        return str;
    }

}
