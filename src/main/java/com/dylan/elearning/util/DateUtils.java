package com.dylan.elearning.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Chuyển từ chuỗi sang Date.
     *
     * @param date chuỗi ngày theo định dạng yyyy-MM-dd
     * @return Date
     */
    public static Date stringToDate(String date) {
        if (date == null || date.trim().isEmpty()) return null;

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }


    /**
     * Chuyển từ chuỗi sang Date với thời gian.
     *
     * @param dateTimeString chuỗi ngày và giờ theo định dạng yyyy-MM-dd HH:mm:ss
     * @return Date
     */
    public static Date stringToDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) return null;

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return formatter.parse(dateTimeString);
        } catch (ParseException e) {
            System.err.println("Error parsing date and time: " + e.getMessage());
            return null;
        }
    }

    /**
     * Chuyển từ Date sang chuỗi theo định dạng yyyy-MM-dd.
     *
     * @param date Date
     * @return chuỗi ngày theo định dạng yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        if (date == null) return null;

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }

    /**
     * Chuyển từ Date sang chuỗi theo định dạng yyyy-MM-dd HH:mm:ss.
     *
     * @param date Date
     * @return chuỗi ngày và giờ theo định dạng yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        if (date == null) return null;

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        return formatter.format(date);
    }

    /**
     * Tính khoảng cách giữa hai Date theo ngày.
     *
     * @param startDate ngày bắt đầu
     * @param endDate   ngày kết thúc
     * @return số ngày giữa hai ngày
     */
    public static long daysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return 0;

        long diffInMillis = endDate.getTime() - startDate.getTime();
        return diffInMillis / (24 * 60 * 60 * 1000);
    }

    /**
     * Lấy ngày hiện tại dưới dạng Date.
     *
     * @return Date của ngày hiện tại
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * Kiểm tra xem một năm có phải năm nhuận hay không.
     *
     * @param year năm cần kiểm tra
     * @return true nếu là năm nhuận, ngược lại false
     */
    public static boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        int daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        return daysInYear > 365;
    }

    /**
     * Thêm số ngày vào một Date.
     *
     * @param date Date ban đầu
     * @param days số ngày cần thêm
     * @return Date sau khi thêm số ngày
     */
    public static Date addDays(Date date, int days) {
        if (date == null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * Trừ số ngày từ một Date.
     *
     * @param date Date ban đầu
     * @param days số ngày cần trừ
     * @return Date sau khi trừ số ngày
     */
    public static Date subtractDays(Date date, int days) {
        return addDays(date, -days);
    }
}
