package com.bamboo.module;

import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Festival {

    public static void initializeFestival(Label nextfestival) {
        LocalDate today = LocalDate.now();
        List<Map.Entry<String, LocalDate>> holidayList = new ArrayList<>();

        // 添加节日到列表中

        holidayList.add(Map.entry("父亲节", calculateFathersDay(today.getYear())));
        holidayList.add(Map.entry("中秋节", LocalDate.of(today.getYear(), Month.SEPTEMBER, 21)));
        holidayList.add(Map.entry("国庆节", LocalDate.of(today.getYear(), Month.OCTOBER, 1)));
        holidayList.add(Map.entry("万圣节", LocalDate.of(today.getYear(), Month.OCTOBER, 31)));
        holidayList.add(Map.entry("感恩节", calculateThanksgiving(today.getYear())));
        holidayList.add(Map.entry("圣诞节", LocalDate.of(today.getYear(), Month.DECEMBER, 25)));
        holidayList.add(Map.entry("元旦", LocalDate.of(today.getYear(), Month.JANUARY, 1)));
        holidayList.add(Map.entry("春节", LocalDate.of(today.getYear(), Month.FEBRUARY, 12)));
        holidayList.add(Map.entry("情人节", LocalDate.of(today.getYear(), Month.FEBRUARY, 14)));
        holidayList.add(Map.entry("复活节", calculateEaster(today.getYear())));
        holidayList.add(Map.entry("清明节", LocalDate.of(today.getYear(), Month.APRIL, 4)));
        holidayList.add(Map.entry("劳动节", LocalDate.of(today.getYear(), Month.MAY, 1)));
        holidayList.add(Map.entry("青年节", LocalDate.of(today.getYear(), Month.MAY, 4)));
        holidayList.add(Map.entry("母亲节", calculateMothersDay(today.getYear())));
        holidayList.add(Map.entry("儿童节", LocalDate.of(today.getYear(), Month.JUNE, 1)));
        holidayList.add(Map.entry("端午节", LocalDate.of(today.getYear(), Month.JUNE, 14)));

        // 计算每个节日距离今天的天数并进行排序
        holidayList.sort(Comparator.comparing(entry -> {
            LocalDate date = entry.getValue();
            if (date.isBefore(today)) {
                date = date.plusYears(1);
            }
            return ChronoUnit.DAYS.between(today, date);
        }));

        // 将排序后的节日插入到LinkedHashMap中
        Map<String, LocalDate> holidays = new LinkedHashMap<>();
        for (Map.Entry<String, LocalDate> entry : holidayList) {
            holidays.put(entry.getKey(), entry.getValue().isBefore(today) ? entry.getValue().plusYears(1) : entry.getValue());
        }

        LocalDate nearestHolidayDate = null;
        String nearestHolidayName = "";
        long minDaysUntil = Long.MAX_VALUE;
        boolean isHolidayToday = false;

        for (Map.Entry<String, LocalDate> entry : holidays.entrySet()) {
            String holidayName = entry.getKey();
            LocalDate holidayDate = entry.getValue();

            long daysUntil = ChronoUnit.DAYS.between(today, holidayDate);

            // 检查今天是否是节日
            if (daysUntil == 0) {
                isHolidayToday = true;
                nearestHolidayName = holidayName;
                break;
            }

            // 更新最近节日的信息
            if (daysUntil < minDaysUntil) {
                minDaysUntil = daysUntil;
                nearestHolidayName = holidayName;
                nearestHolidayDate = holidayDate;
            }
        }

        if (isHolidayToday) {
            nextfestival.setText("今天是 " + nearestHolidayName + "，节日快乐！");
        } else if (nearestHolidayDate != null) {
            nextfestival.setText("距离 " + nearestHolidayName + " 还有 " + minDaysUntil + " 天");
        }
    }

    // 计算复活节日期的辅助方法
    private static LocalDate calculateEaster(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(year, month, day);
    }

    // 计算感恩节日期的辅助方法（11月的第四个星期四）
    private static LocalDate calculateThanksgiving(int year) {
        LocalDate fourthThursday = LocalDate.of(year, Month.NOVEMBER, 1)
                .with(java.time.temporal.TemporalAdjusters.dayOfWeekInMonth(4, java.time.DayOfWeek.THURSDAY));
        return fourthThursday;
    }

    // 计算母亲节日期的辅助方法（5月的第二个星期日）
    private static LocalDate calculateMothersDay(int year) {
        LocalDate secondSunday = LocalDate.of(year, Month.MAY, 1)
                .with(java.time.temporal.TemporalAdjusters.dayOfWeekInMonth(2, java.time.DayOfWeek.SUNDAY));
        return secondSunday;
    }

    // 计算父亲节日期的辅助方法（6月的第三个星期日）
    private static LocalDate calculateFathersDay(int year) {
        LocalDate thirdSunday = LocalDate.of(year, Month.JUNE, 1)
                .with(java.time.temporal.TemporalAdjusters.dayOfWeekInMonth(3, java.time.DayOfWeek.SUNDAY));
        return thirdSunday;
    }

}
