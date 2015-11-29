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
        chart.setEventRenderer(new RaisedColored(Color.green));
        chart.getComponent().setPreferredSize(new Dimension(1000, 500));
        frame.add(chart.getComponent());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class RaisedColored extends EventRenderer.Default<BasicEvent> {

        public RaisedColored(Color green) {
            setBackground(green);
            setOpaque(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }
}
