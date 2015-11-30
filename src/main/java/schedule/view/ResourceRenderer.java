package schedule.view;

import schedule.model.Resource;

import javax.swing.*;
import java.awt.*;

public interface ResourceRenderer<R extends Resource> {

    Component getRenderingComponent(R resource);

    class Default<R extends Resource> extends JLabel implements ResourceRenderer<R> {

        public Default() {
            setFont(getFont().deriveFont(Font.PLAIN, 10f));
        }

        @Override
        public Component getRenderingComponent(R resource) {
            setText(getTextFromResource(resource));
            return this;
        }

        protected String getTextFromResource(R resource) {
            return resource.toString();
        }
    }
}
