package schedule.chart;

import schedule.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public interface TaskRenderer<TaskType extends Task> {

    JComponent getRenderingComponent(TaskType task);

    class Default<TaskType extends Task> extends JPanel implements TaskRenderer<TaskType> {
        protected DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        protected JLabel left = createLabel(JLabel.LEFT);
        protected JLabel center = createLabel(JLabel.CENTER);
        protected JLabel right = createLabel(JLabel.RIGHT);

        public Default() {
            super(new BorderLayout(), false);
            setOpaque(false);
            add(left, BorderLayout.WEST);
            add(center, BorderLayout.CENTER);
            add(right, BorderLayout.EAST);
            center.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            setBorder(BorderFactory.createLineBorder(Color.black));
        }

        @Override
        public JComponent getRenderingComponent(TaskType task) {
            left.setText(task.getStart().format(format));
            center.setText(getTextFromTask(task));
            right.setText(task.getEnd().format(format));
            return this;
        }

        protected String getTextFromTask(TaskType event) {
            return event.toString();
        }

        JLabel createLabel(int alignment) {
            JLabel label = new JLabel("", alignment);
            label.setFont(getFont().deriveFont(Font.PLAIN, 10f));
            return label;
        }
    }
}
