package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;

import java.awt.event.MouseEvent;

public class ReassignWithDragAndDrop<R extends Resource, TaskType extends Task> extends MouseInteractions.Default<R, TaskType> {

    final ReassignHandler<R, TaskType> reassignHandler;

    public ReassignWithDragAndDrop(ReassignHandler<R, TaskType> reassignHandler) {
        this.reassignHandler = reassignHandler;
    }

    @Override
    public void taskDroppedOnRow(TaskType task, R resource, MouseEvent e) {
        reassignHandler.reassign(task, resource);
    }

    public static <R extends Resource, TaskType extends Task> ReassignWithDragAndDrop<R, TaskType> withHandler(ReassignHandler<R, TaskType> reassignHandler) {
        return new ReassignWithDragAndDrop<>(reassignHandler);
    }

    public interface ReassignHandler<R extends Resource, TaskType extends Task> {
        void reassign(TaskType task, R newResource);
    }
}
