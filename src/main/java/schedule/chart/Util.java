package schedule.chart;

import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    static ZonedDateTime toMidnight(ZonedDateTime time) {
        time = time.minusNanos(time.getNano());
        time = time.minusSeconds(time.getSecond());
        time = time.minusMinutes(time.getMinute());
        time = time.minusHours(time.getHour());
        return time;
    }

    static void renderDayLines(Graphics g, ScheduleChart scheduleChart, int height, DateTimeFormatter formatter) {
        ZonedDateTime time = toMidnight(scheduleChart.model.getStart());
        while (time.isBefore(scheduleChart.model.getEnd())) {
            time = time.plusDays(1);
            int x = scheduleChart.timeToX(time);
            g.setColor(Color.lightGray);
            g.drawLine(x, 0, x, height);
            if (formatter != null) {
                g.drawString(time.format(formatter), x + 2, height - 2);
            }
        }
    }
}
