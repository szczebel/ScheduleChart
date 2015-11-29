package schedule;

import javax.swing.*;
import java.awt.*;

interface ResourceRenderer<R extends Resource> {

    Component getRenderingComponent(R resource);

    class Default<R extends Resource> extends JLabel implements ResourceRenderer<R> {

        public Default() {
            setFont(getFont().deriveFont(Font.PLAIN, 10f));
        }

        @Override
        public Component getRenderingComponent(R resource) {
            setText(resource.toString());
            return this;
        }
    }
}
