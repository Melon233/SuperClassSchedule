package com.bamboo.module;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import com.bamboo.module.ClassSchedule.Course;

import javafx.scene.control.Label;

public class DaySchedule {
    public static int realWeek = -1;
    public static int realDay = -1;

    public static void initializeDaySchedule(Label[] dayScheduLabels, Label hasClassLabel) {
        long dayDiff = ChronoUnit.DAYS.between(ClassSchedule.termStartDate, LocalDate.now());
        realWeek = (int) (dayDiff / 7) + 1;
        realDay = (int) (dayDiff % 7 + 1);
        Course tp;
        int cnt = 0;
        for (int i = 0; i < 4; i++) {
            tp = ClassSchedule.classDataMatrix[realDay - 1][i];
            if (tp == null) {
                dayScheduLabels[i].setText("×");
                cnt++;
                continue;
            }
            if (tp.hasCourseWeek[realWeek - 1]) {
                dayScheduLabels[i].setText(String.format("%s\n%s\n%s", tp.courseName, tp.coursePosition, tp.teacher));
            } else {
                dayScheduLabels[i].setText("×");
                cnt++;
            }
        }
        if (cnt == 4) {
            hasClassLabel.setText("今日无课~");
        }

    }
}
