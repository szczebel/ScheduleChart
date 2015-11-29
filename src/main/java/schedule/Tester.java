package schedule;

import javax.swing.*;
import java.time.ZonedDateTime;

public class Tester {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Schedule");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BasicScheduleModel<BasicResource, BasicEvent> scheduleModel = new BasicScheduleModel<>();
        ZonedDateTime start = ZonedDateTime.now().minusDays(60);
        ZonedDateTime end = ZonedDateTime.now();
        new DataGenerator().generate(scheduleModel, start, end);
        frame.add(new ScheduleChart<>(scheduleModel, start, end).getComponent());


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
