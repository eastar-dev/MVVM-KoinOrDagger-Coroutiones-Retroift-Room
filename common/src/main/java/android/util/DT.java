package android.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author r
 */
@SuppressLint("SimpleDateFormat")
public class DT {



    public static final long TIMEGAP = +32400000L;
    public static final long DAY1 = 86400000L;

    public static String now_yyyymmdd() {
        return DT.yyyymmdd(System.currentTimeMillis());
    }

    public static String now_yyyymmddhhmmss() {
        return DT.yyyymmddhhmmss(System.currentTimeMillis());
    }

    public static String yyyymmdd(long milliseconds) {
        return DT.format(milliseconds, DT.yyyymmdd);
    }
    public static String yyyymmddhhmmss(long milliseconds) {
        return DT.format(milliseconds, DT.yyyymmddhhmmss);
    }
    public static String mmddhhmmss__Now() {
        return DT.format(System.currentTimeMillis(), DT.mmddhhmmss__);
    }

    public static long ST(long milliseconds) {
        return ((milliseconds + TIMEGAP) / DAY1 * DAY1) - TIMEGAP;
    }

    public static long move(long milliseconds, int field, int distance) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        c.add(field, distance);
        return c.getTimeInMillis();
    }

    public static String format(long milliseconds, SimpleDateFormat to) {
        try {
            return to.format(new Date(milliseconds));
        } catch (Exception e) {
//			e.printStackTrace();
            return "";
        }
    }

    public static String format(String date, SimpleDateFormat from, SimpleDateFormat to) {
        try {
            return to.format(from.parse(date));
        } catch (ParseException e) {
//			e.printStackTrace();
        }
        return date;
    }

    public static long parse(String date, SimpleDateFormat from) {
        try {
            return from.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static final SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");

    public static final SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat yyyy__ = new SimpleDateFormat("yyyy년");

    public static final SimpleDateFormat weekofmonth = new SimpleDateFormat("W");

    public static final SimpleDateFormat mmdd__ = new SimpleDateFormat("MM월dd일");
    public static final SimpleDateFormat mmdd_2 = new SimpleDateFormat("MM.dd");

    public static final SimpleDateFormat mm__ = new SimpleDateFormat("MM월");
    public static final SimpleDateFormat m__ = new SimpleDateFormat("M월");

    public static final SimpleDateFormat dd = new SimpleDateFormat("dd");
    public static final SimpleDateFormat dd__ = new SimpleDateFormat("dd일");

    public static final SimpleDateFormat emdhs = new SimpleDateFormat("(E)M/d HH:mm");
    public static final SimpleDateFormat mdehs = new SimpleDateFormat("M/d(E) HH:mm");

    public static final SimpleDateFormat mde = new SimpleDateFormat("M/d(E)");
    public static final SimpleDateFormat dayofweek = new SimpleDateFormat("E");
    public static final SimpleDateFormat dayofweek__ = new SimpleDateFormat("E요일");

    public static final SimpleDateFormat yyyy_mmdd = new SimpleDateFormat("yyyy\nMM.dd");
    public static final SimpleDateFormat yyyy__mmdd = new SimpleDateFormat("yyyy.\nMM.dd");

    public static final SimpleDateFormat yyyymmddE__ = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREA);

    public static final SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat yyyymmdd_ = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat yyyymmdd__ = new SimpleDateFormat("yyyy년 MM월 dd일");
    public static final SimpleDateFormat yyyymmdd_1 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat yyyymmdd_2 = new SimpleDateFormat("yyyy.MM.dd");

    public static final SimpleDateFormat yyyymmddahmm_ = new SimpleDateFormat("yyyy/MM/dd a hh:mm");

    public static final SimpleDateFormat yyyymmddhhmm__ = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");

    public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat yyyymmddhhmmss_ = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat yyyymmddhhmmss_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat yyyymmddhhmmss_2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static final SimpleDateFormat mmddhhmmss__ = new SimpleDateFormat("MM월dd일 HH:mm");

    public static final SimpleDateFormat ahhmm = new SimpleDateFormat("a hh:mm");
    public static final SimpleDateFormat hhmmss = new SimpleDateFormat("HHmmss");
    public static final SimpleDateFormat mmss = new SimpleDateFormat("mm:ss");
    public static final SimpleDateFormat ms = new SimpleDateFormat("m:s");

}
