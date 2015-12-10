package schedule.demo.simple;

import schedule.basic.BasicResource;
import schedule.basic.BasicTask;
import schedule.basic.GenericScheduleModel;
import schedule.interaction.InstantTooltips;
import schedule.interaction.MenuOnRightClick;
import schedule.interaction.MouseInteractions;
import schedule.interaction.MouseInteractionsDispatcher;
import schedule.view.ScheduleView;
import schedule.view.TaskRenderer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static schedule.interaction.MouseInteractions.Aggregate;

public class SimpleDemo {

    private ScheduleView<BasicResource, BasicTask> chart;

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Schedule");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new SimpleDemo().buildContent());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private GenericScheduleModel<BasicResource, BasicTask> scheduleModel;

    JComponent buildContent() {
        scheduleModel = new GenericScheduleModel<>();
        ZonedDateTime start = ZonedDateTime.now().minusDays(14);
        ZonedDateTime end = ZonedDateTime.now().plusDays(14);
        new DataGenerator().generate(scheduleModel, start, end);
        chart = createChart();
        return northAndCenter(toolbar(), chart.getComponent());
    }

    private ScheduleView<BasicResource, BasicTask> createChart() {
        ScheduleView<BasicResource, BasicTask> chart = new ScheduleView<>(scheduleModel);
        chart.setRowHeight(14);
        chart.setTaskRenderer(dayOfWeekColors());
        chart.setMouseInteractions(
                Aggregate.of(
                        InstantTooltips.renderWith(dayOfWeekColors()),
                        dispatch()
                                .whenTaskDroppedThen(this::reassign)
                                .whenRowClickedThen(this::newMeeting),
                        toRightClickMenu()
                                .addTaskMenuAction("Delete this", this::unassign)
                                .addResourceMenuAction("Plan meeting in this room", this::newMeeting)
                )
        );
        chart.enableDragAndDrop(true);
        chart.getComponent().setPreferredSize(new Dimension(1000, 500));
        return chart;
    }

    private MenuOnRightClick<BasicResource, BasicTask> toRightClickMenu() {
        return MenuOnRightClick.<BasicResource, BasicTask>create();
    }

    private MouseInteractionsDispatcher<BasicResource, BasicTask> dispatch() {
        return MouseInteractionsDispatcher.<BasicResource, BasicTask>create();
    }

    private void newMeeting(BasicResource room) {
        //todo do sth here: scheduleModel.assign(room, new BasicTask(...));
    }

    private void reassign(MouseInteractions.DragAndDropTrio<BasicResource, BasicTask> dragAndDropTrio) {
        if (JOptionPane.showConfirmDialog(chart.getComponent(), "Are you sure you want to reassign?", "Please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
            scheduleModel.reassign(dragAndDropTrio.task, dragAndDropTrio.targetResource);
    }

    private void unassign(BasicTask task) {
        if (JOptionPane.showConfirmDialog(chart.getComponent(), "Are you sure you want to delete?", "Please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
            scheduleModel.unassign(task);
    }

    private JComponent northAndCenter(JComponent toolbar, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JComponent toolbar() {
        JPanel panel = new JPanel(new FlowLayout());


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
        panel.add(new JButton(new AbstractAction("Delete selected meetings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chart.getSelection().forEach(scheduleModel::unassign);
            }
        }));
        panel.add(new JLabel(" Ctrl+wheel to zoom in/out"));
        return panel;
    }

    TaskRenderer<BasicTask> dayOfWeekColors() {
        Map<DayOfWeek, Color> colorMap = new HashMap<>();
        colorMap.put(DayOfWeek.MONDAY, Color.green);
        colorMap.put(DayOfWeek.TUESDAY, Color.green);
        colorMap.put(DayOfWeek.WEDNESDAY, Color.green);
        colorMap.put(DayOfWeek.THURSDAY, Color.green);
        colorMap.put(DayOfWeek.FRIDAY, Color.green);
        colorMap.put(DayOfWeek.SATURDAY, Color.cyan);
        colorMap.put(DayOfWeek.SUNDAY, Color.magenta);
        TaskRenderer.Default<BasicTask> renderer = new TaskRenderer.Default<BasicTask>() {
            @Override
            public JComponent getRenderingComponent(BasicTask task) {
                setBackground(colorMap.get(task.getStart().getDayOfWeek()));
                return super.getRenderingComponent(task);
            }
        };
        renderer.setOpaque(true);
        renderer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        return renderer;
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
