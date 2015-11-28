package schedule;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.swing.*;
import java.time.ZonedDateTime;

public class Tester {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Schedule");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ScheduleModel<BasicResource, BasicEvent> scheduleModel = new BasicScheduleModel<>();

        frame.add(new ScheduleChart<>(scheduleModel).getComponent());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class ScheduleChart<R extends Resource, E extends Event> {
        private final ScheduleModel<R, E> model;
        private final JComponent component;

        public ScheduleChart(ScheduleModel<R, E> scheduleModel) {
            model = scheduleModel;
            component = buildComponent();
        }

        private JComponent buildComponent() {
            return new JPanel();
        }

        public JComponent getComponent() {
            return component;
        }
    }

    static interface ScheduleModel<R extends Resource, E extends Event> {
        static interface Listener {
            void dataChanged();
        }
    }

    static interface Resource {

    }

    static interface Event {

        ZonedDateTime getStart();

        ZonedDateTime getEnd();
    }

    static class BasicScheduleModel<R extends Resource, E extends Event> implements ScheduleModel<R, E> {
        final Multimap<R, E> data = HashMultimap.create();

    }

    static class BasicResource implements Resource {
        final String name;

        BasicResource(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class BasicEvent implements Event {
        final ZonedDateTime start, end;
        final String name;

        BasicEvent(ZonedDateTime start, ZonedDateTime end, String name) {
            this.start = start;
            this.end = end;
            this.name = name;
        }

        public ZonedDateTime getStart() {
            return start;
        }

        public ZonedDateTime getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
