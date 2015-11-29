package schedule;

import schedule.chart.EventRenderer;
import schedule.chart.ScheduleChart;
import schedule.model.BasicEvent;
import schedule.model.BasicResource;
import schedule.model.BasicScheduleModel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.time.ZonedDateTime;

public class Tester {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Schedule");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BasicScheduleModel<BasicResource, BasicEvent> scheduleModel = new BasicScheduleModel<>();
        ZonedDateTime start = ZonedDateTime.now().minusDays(60);
        ZonedDateTime end = ZonedDateTime.now();
        new DataGenerator().generate(scheduleModel, start, end);
        ScheduleChart<BasicResource, BasicEvent> chart = new ScheduleChart<>(scheduleModel);
        chart.setRowHeight(14);
        chart.setEventRenderer(new Green());
        frame.add(chart.getComponent());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        end = end.minusNanos(end.getNano());
        end = end.minusSeconds(end.getSecond());
        end = end.minusMinutes(end.getMinute());
        end = end.minusHours(end.getHour());
        System.out.println(end);
    }

    private static class Green extends EventRenderer.Default<BasicEvent> {

        public Green() {
            setBackground(Color.green);
            setOpaque(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }
}
