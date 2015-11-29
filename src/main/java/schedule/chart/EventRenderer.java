package schedule.chart;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public interface EventRenderer<E extends schedule.model.Event> {

    Component getRenderingComponent(E event);

    class Default<E extends schedule.model.Event> extends JPanel implements EventRenderer<E> {
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
            setBorder(BorderFactory.createLineBorder(Color.black));
        }

        @Override
        public Component getRenderingComponent(E event) {
            left.setText(event.getStart().format(format));
            center.setText(getTextFromEvent(event));
            right.setText(event.getEnd().format(format));
            return this;
        }

        protected String getTextFromEvent(E event) {
            return event.toString();
        }

        JLabel createLabel(int alignment) {
            JLabel label = new JLabel("", alignment);
            label.setFont(getFont().deriveFont(Font.PLAIN, 10f));
            return label;
        }
    }
}
