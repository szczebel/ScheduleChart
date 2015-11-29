package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;

import java.awt.event.MouseEvent;

public interface Interactions<R extends Resource, TaskType extends Task> {
    void mouseOverTask(TaskType task, MouseEvent e);

    void mouseClickedOnTask(TaskType task, MouseEvent e);

    void mouseClickedOnRow(R resource, MouseEvent e);

    void mouseOverRow(R resource, MouseEvent e);

    class Default<R extends Resource, TaskType extends Task> implements Interactions<R, TaskType> {

        @Override
        public void mouseOverTask(TaskType task, MouseEvent e) {
        }

        @Override
        public void mouseClickedOnTask(TaskType task, MouseEvent e) {
        }

        @Override
        public void mouseClickedOnRow(R resource, MouseEvent e) {
        }

        @Override
        public void mouseOverRow(R resource, MouseEvent e) {

        }
    }
}
