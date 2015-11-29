package schedule.chart;

import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    public static void renderRowBackground(Graphics g, int rowNumber, int width, int rowHeight, int rowMargin) {
        int y = rowNumber * (rowHeight + 2 * rowMargin);
        g.setColor(rowNumber % 2 == 0 ? Color.white : Color.decode("0xf9f9f9"));
        g.fillRect(0, y, width, rowHeight + 2 * rowMargin);
    }

    static ZonedDateTime toMidnight(ZonedDateTime time) {
        time = time.minusNanos(time.getNano());
        time = time.minusSeconds(time.getSecond());
        time = time.minusMinutes(time.getMinute());
        time = time.minusHours(time.getHour());
        return time;
    }

    static void renderDayLines(Graphics g, ScheduleChart scheduleChart, int height, DateTimeFormatter formatter) {
        ZonedDateTime time = toMidnight(scheduleChart.start);
        while (time.isBefore(scheduleChart.end)) {
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
