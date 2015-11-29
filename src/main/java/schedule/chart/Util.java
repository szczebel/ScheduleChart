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

    static void renderDayLines(Graphics g, int height, DateTimeFormatter formatter, ScheduleChart.Configuration configuration, ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime time = toMidnight(start);
        while (time.isBefore(end)) {
            time = time.plusDays(1);
            int x = configuration.timeToX(time);
            g.setColor(Color.lightGray);
            g.drawLine(x, 0, x, height);
            if (formatter != null) {
                g.drawString(time.format(formatter), x + 2, height - 2);
            }
        }
    }
}
