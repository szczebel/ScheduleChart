package schedule.demo.meetingroom;

import schedule.model.GenericScheduleModel;
import schedule.view.ScheduleView;
import schedule.view.TaskRenderer;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class RoomSchedule {

    public static void main(String[] args) {
        runWith(new RoomSchedule());
    }

    static void runWith(RoomSchedule roomSchedule) {
        final JFrame frame = new JFrame("Schedule");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(roomSchedule.buildContent());
        SwingUtilities.invokeLater(() -> {
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
        );
    }

    JComponent buildContent() {
        ZonedDateTime start = ZonedDateTime.now().minusDays(14);
        ZonedDateTime end = ZonedDateTime.now().plusDays(14);
        GenericScheduleModel<Room, Meeting> model = new GenericScheduleModel<>();
        new DataGenerator().generate(model, start, end);
        ScheduleView<Room, Meeting> chart = createChart(model);
        return chart.getComponent();
    }

    ScheduleView<Room, Meeting> createChart(GenericScheduleModel<Room, Meeting> model) {
        ScheduleView<Room, Meeting> chart = new ScheduleView<>(model);
        chart.setRowHeight(14);
        chart.setTaskRenderer(dayOfWeekColors());
        chart.getComponent().setPreferredSize(new Dimension(1000, 500));
        return chart;
    }


    TaskRenderer<Meeting> dayOfWeekColors() {
        Map<DayOfWeek, Color> colorMap = new HashMap<>();
        colorMap.put(DayOfWeek.MONDAY, Color.green);
        colorMap.put(DayOfWeek.TUESDAY, Color.green);
        colorMap.put(DayOfWeek.WEDNESDAY, Color.green);
        colorMap.put(DayOfWeek.THURSDAY, Color.green);
        colorMap.put(DayOfWeek.FRIDAY, Color.green);
        colorMap.put(DayOfWeek.SATURDAY, Color.cyan);
        colorMap.put(DayOfWeek.SUNDAY, Color.magenta);
        TaskRenderer.Default<Meeting> renderer = new TaskRenderer.Default<schedule.demo.meetingroom.Meeting>(){
            @Override
            public JComponent getRenderingComponent(Meeting task) {
                setBackground(colorMap.get(task.getStart().getDayOfWeek()));
                return super.getRenderingComponent(task);
            }
        };
        renderer.setOpaque(true);
        return renderer;
    }

}
