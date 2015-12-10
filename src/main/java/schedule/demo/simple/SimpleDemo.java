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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.time.ZonedDateTime;
import java.util.function.Consumer;

import static schedule.interaction.MouseInteractions.Aggregate;

public class SimpleDemo {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Schedule");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BasicScheduleModel<BasicResource, BasicTask> scheduleModel = new BasicScheduleModel<>();
        ZonedDateTime start = ZonedDateTime.now().minusDays(30);
        ZonedDateTime end = ZonedDateTime.now().plusDays(30);
        new DataGenerator().generate(scheduleModel, start, end);
        ScheduleView<BasicResource, BasicTask> chart = createChart(scheduleModel);

        frame.add(northAndCenter(toolbar(chart, scheduleModel), chart.getComponent()));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    protected static ScheduleView<BasicResource, BasicTask> createChart(BasicScheduleModel<BasicResource, BasicTask> scheduleModel) {
        ScheduleView<BasicResource, BasicTask> chart = new ScheduleView<>(scheduleModel);
        chart.setRowHeight(14);
        chart.setTaskRenderer(new RaisedColored(Color.green));
        chart.setMouseInteractions(
                Aggregate.of(
                        InstantTooltips.renderWith(new RaisedColored(Color.magenta)),
                        ReassignWithDragAndDrop.withHandler(scheduleModel),
                        new RightClickHandler(scheduleModel)
                )
        );
        chart.enableDragAndDrop(true);
        chart.getComponent().setPreferredSize(new Dimension(1000, 500));
        return chart;
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
        panel.add(new JLabel("Find meetings ending with:"));
        JTextField meetingFilter = new JTextField(10);
        meetingFilter.getDocument().addDocumentListener(new TextChangeListener(meetingFilter, text ->
                scheduleModel.setTaskFilter(task -> text.isEmpty() || task.getName().endsWith(text))
        ));

        panel.add(meetingFilter);
        panel.add(new JLabel("Find rooms ending with:"));
        JTextField roomFilter = new JTextField(10);
        roomFilter.getDocument().addDocumentListener(new TextChangeListener(roomFilter, text ->
                scheduleModel.setResourceFilter(resource -> text.isEmpty() || resource.getName().endsWith(text))
        ));
        panel.add(roomFilter);
        return panel;
    }

    private static class RaisedColored extends TaskRenderer.Default<BasicTask> {

        public RaisedColored(Color green) {
            setBackground(green);
            setOpaque(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }

    private static class RightClickHandler extends MouseInteractions.Default<BasicResource, BasicTask> {
        private final BasicScheduleModel<BasicResource, BasicTask> scheduleModel;

        public RightClickHandler(BasicScheduleModel<BasicResource, BasicTask> scheduleModel) {
            this.scheduleModel = scheduleModel;
        }

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

    private static class TextChangeListener implements DocumentListener {
        private final JTextField textField;
        private final Consumer<String> consumer;

        public TextChangeListener(JTextField textField, Consumer<String> consumer) {
            this.textField = textField;
            this.consumer = consumer;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            onChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onChange();
        }

        void onChange() {
            consumer.accept(textField.getText().trim());
        }
    }
}
