package schedule.demo.simple;

import schedule.basic.BasicResource;
import schedule.basic.BasicScheduleModel;
import schedule.basic.BasicTask;
import schedule.interaction.InstantTooltips;
import schedule.interaction.MouseInteractions;
import schedule.interaction.ReassignWithDragAndDrop;
import schedule.view.ScheduleView;
import schedule.view.TaskRenderer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
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
        chart.setMouseInteractions(
                Aggregate.of(
                        InstantTooltips.renderWith(new RaisedColored(Color.magenta)),
                        ReassignWithDragAndDrop.withHandler(scheduleModel),
                        new MouseInteractions.Default<BasicResource, BasicTask>() {
                            @Override
                            public void mouseClickedOnTask(BasicTask task, MouseEvent e) {
                                if (e.getButton() == 3) {
                                    JPopupMenu popupMenu = new JPopupMenu("Actions");
                                    popupMenu.add(new JMenuItem(new AbstractAction("Delete") {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            scheduleModel.unassign(task);
                                        }
                                    }));
                                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                                }
                            }
                        }
                )
        );
        chart.enableDragAndDrop(true);


        chart.getComponent().setPreferredSize(new Dimension(1000, 500));
        frame.add(northAndCenter(toolbar(chart, scheduleModel), chart.getComponent()));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JComponent northAndCenter(JComponent toolbar, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private static JComponent toolbar(ScheduleView<BasicResource, BasicTask> chart, BasicScheduleModel<BasicResource, BasicTask> scheduleModel) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JButton(new AbstractAction("Delete selection") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chart.getSelection().forEach(scheduleModel::unassign);
            }
        }));
        return panel;
    }

    private static class RaisedColored extends TaskRenderer.Default<BasicTask> {

        public RaisedColored(Color green) {
            setBackground(green);
            setOpaque(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }
}
