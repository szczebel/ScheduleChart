package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;

import java.awt.event.MouseEvent;

@SuppressWarnings("unused")
public class EmptyInteractions<R extends Resource, TaskType extends Task> implements MouseInteractions<R, TaskType> {

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
    public void mouseOverEmptySpace(R resource, MouseEvent e) {
    }

    @Override
    public void taskDraggedOverRow(R original, TaskType task, R target, MouseEvent e) {
    }

    @Override
    public void taskDroppedOnRow(R original, TaskType task, R target, MouseEvent e) {
    }
}
