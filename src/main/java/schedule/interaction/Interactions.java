package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public interface Interactions<R extends Resource, TaskType extends Task> {
    void mouseOverTask(TaskType task, MouseEvent e);

    void mouseClickedOnTask(TaskType task, MouseEvent e);

    void mouseClickedOnChart(R row, MouseEvent e);

    class Default<R extends Resource, TaskType extends Task> implements Interactions<R, TaskType> {

        @Override
        public void mouseOverTask(TaskType task, MouseEvent e) {
            Component sender = e.getComponent();
            PopupFactory.getSharedInstance().getPopup(sender, new JLabel("Aha"), e.getX(), e.getY()).show();
        }

        @Override
        public void mouseClickedOnTask(TaskType task, MouseEvent e) {

        }

        @Override
        public void mouseClickedOnChart(R row, MouseEvent e) {

        }
    }
}
