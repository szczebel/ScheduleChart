package schedule;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

interface EventRenderer<E extends Event> {

    Component getRenderingComponent(E event);

    class Default<E extends Event> extends JLabel implements EventRenderer<E> {
        protected static final DateTimeFormatter dayHourMins = DateTimeFormatter.ofPattern("dd.MMM HH:mm");

        public Default() {
            setFont(getFont().deriveFont(Font.PLAIN, 10f));
            setHorizontalAlignment(CENTER);
            setBorder(BorderFactory.createLineBorder(Color.black));
        }

        @Override
        public Component getRenderingComponent(E event) {
            setText(event + " , " + event.getStart().format(dayHourMins) + " - " + event.getEnd().format(dayHourMins));
            return this;
        }
    }
}
