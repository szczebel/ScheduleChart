package schedule.demo.meetingroom;

import schedule.interaction.InstantTooltips;
import schedule.interaction.MenuOnRightClick;
import schedule.interaction.MouseInteractions;
import schedule.interaction.MouseInteractionsDispatcher;
import schedule.model.GenericScheduleModel;
import schedule.view.ScheduleView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import static schedule.interaction.MouseInteractions.Aggregate;

public class InteractiveRoomSchedule extends RoomSchedule {

    private GenericScheduleModel<Room, Meeting> model;
    private ScheduleView<Room, Meeting> chart;

    public static void main(String[] args) {
        runWith(new InteractiveRoomSchedule());
    }

    @Override
    ScheduleView<Room, Meeting> createChart(GenericScheduleModel<Room, Meeting> model) {
        this.chart = super.createChart(model);
        this.model = model;
        chart.setRowHeight(14);
        chart.setMouseInteractions(
                Aggregate.of(
                        InstantTooltips.renderWith(dayOfWeekColors()),
                        dispatch()
                                .whenTaskDroppedThen(this::reassign)
                                .whenRowClickedThen(this::newMeeting),
                        rightClickMenu()
                                .addTaskMenuAction("Delete this", this::unassign)
                                .addResourceMenuAction("Plan meeting in this room", this::newMeeting)
                )
        );
        chart.enableDragAndDrop(true);

        return chart;
    }

    @Override
    JComponent buildContent() {
        return northAndCenter(toolbar(), super.buildContent());
    }


    private MenuOnRightClick<Room, Meeting> rightClickMenu() {
        return MenuOnRightClick.<Room, Meeting>create();
    }

    private MouseInteractionsDispatcher<Room, Meeting> dispatch() {
        return MouseInteractionsDispatcher.<Room, Meeting>create();
    }

    private void newMeeting(Room room) {
        //todo do sth here: scheduleModel.assign(room, new BasicTask(...));
    }

    private void reassign(MouseInteractions.DragAndDropTrio<Room, Meeting> dragAndDropTrio) {
        if (JOptionPane.showConfirmDialog(chart.getComponent(), "Are you sure you want to reassign?", "Please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
            model.reassign(dragAndDropTrio.task, dragAndDropTrio.targetResource);
    }

    private void unassign(Meeting task) {
        if (JOptionPane.showConfirmDialog(chart.getComponent(), "Are you sure you want to delete?", "Please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
            model.unassign(task);
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
                model.setTaskFilter(task -> text.isEmpty() || task.name.endsWith(text))
        ));

        panel.add(meetingFilter);
        panel.add(new JLabel("Find rooms ending with:"));
        JTextField roomFilter = new JTextField(10);
        roomFilter.getDocument().addDocumentListener(new TextChangeListener(roomFilter, text ->
                model.setResourceFilter(resource -> text.isEmpty() || resource.name.endsWith(text))
        ));
        panel.add(roomFilter);
        panel.add(new JButton(new AbstractAction("Delete selected meetings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chart.getSelection().forEach(model::unassign);
            }
        }));
        panel.add(new JLabel(" Ctrl+wheel to zoom in/out"));
        return panel;
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
