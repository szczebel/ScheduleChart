package schedule.demo.simple;

import schedule.basic.BasicResource;
import schedule.basic.BasicScheduleModel;
import schedule.basic.BasicTask;
import schedule.interaction.InstantTooltips;
import schedule.interaction.ReassignWithDragAndDrop;
import schedule.view.ScheduleView;
import schedule.view.TaskRenderer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.time.ZonedDateTime;

import static schedule.interaction.MouseInteractions.Aggregate;

public class SimpleDemo {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Schedule");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BasicScheduleModel<BasicResource, BasicTask> scheduleModel = new BasicScheduleModel<>();
        ZonedDateTime start = ZonedDateTime.now().minusDays(60);
        ZonedDateTime end = ZonedDateTime.now();
        new DataGenerator().generate(scheduleModel, start, end);
        ScheduleView<BasicResource, BasicTask> chart = new ScheduleView<>(scheduleModel);
        chart.setRowHeight(14);
        chart.setTaskRenderer(new RaisedColored(Color.green));
        //noinspection unchecked
        chart.setMouseInteractions(
                Aggregate.of(
                        InstantTooltips.renderWith(new RaisedColored(Color.magenta)),
                        ReassignWithDragAndDrop.withHandler(scheduleModel)
                )
        );


        chart.getComponent().setPreferredSize(new Dimension(1000, 500));
        frame.add(chart.getComponent());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class RaisedColored extends TaskRenderer.Default<BasicTask> {

        public RaisedColored(Color green) {
            setBackground(green);
            setOpaque(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }
}
