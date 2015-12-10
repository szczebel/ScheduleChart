package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;

import java.awt.event.MouseEvent;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class MouseInteractionsDispatcher<R extends Resource, TaskType extends Task> implements MouseInteractions<R, TaskType> {

    Consumer<TaskType> mouseOverTask = t -> {
    };
    Consumer<R> mouseOverNoTask = r -> {
    };
    Consumer<TaskType> taskClicked = t -> {
    };
    Consumer<R> rowClicked = r -> {
    };
    Consumer<DragAndDropTrio<R, TaskType>> taskDragged = t -> {
    };
    Consumer<DragAndDropTrio<R, TaskType>> taskDropped = t -> {
    };

    public MouseInteractionsDispatcher<R, TaskType> whenMouseOverTaskThen(Consumer<TaskType> mouseOverTask) {
        this.mouseOverTask = mouseOverTask;
        return this;
    }

    public MouseInteractionsDispatcher<R, TaskType> whenMouseOverNoTaskThen(Consumer<R> mouseOverNoTask) {
        this.mouseOverNoTask = mouseOverNoTask;
        return this;
    }

    public MouseInteractionsDispatcher<R, TaskType> whenTaskClickedThen(Consumer<TaskType> taskClicked) {
        this.taskClicked = taskClicked;
        return this;
    }

    public MouseInteractionsDispatcher<R, TaskType> whenRowClickedThen(Consumer<R> rowClicked) {
        this.rowClicked = rowClicked;
        return this;
    }

    public MouseInteractionsDispatcher<R, TaskType> whenTaskDraggedThen(Consumer<DragAndDropTrio<R, TaskType>> taskDragged) {
        this.taskDragged = taskDragged;
        return this;
    }

    public MouseInteractionsDispatcher<R, TaskType> whenTaskDroppedThen(Consumer<DragAndDropTrio<R, TaskType>> taskDropped) {
        this.taskDropped = taskDropped;
        return this;
    }

    public static <R extends Resource, TaskType extends Task> MouseInteractionsDispatcher<R, TaskType> create() {
        return new MouseInteractionsDispatcher<>();
    }

    @Override
    public void mouseOverTask(TaskType task, MouseEvent e) {
        mouseOverTask.accept(task);
    }

    @Override
    public void mouseClickedOnTask(TaskType task, MouseEvent e) {
        taskClicked.accept(task);
    }

    @Override
    public void mouseClickedOnRow(R resource, MouseEvent e) {
        rowClicked.accept(resource);
    }

    @Override
    public void mouseOverEmptySpace(R resource, MouseEvent e) {
        mouseOverNoTask.accept(resource);
    }

    @Override
    public void taskDraggedOverRow(R original, TaskType task, R target, MouseEvent e) {
        taskDragged.accept(new DragAndDropTrio<>(original, task, target));
    }

    @Override
    public void taskDroppedOnRow(R original, TaskType task, R target, MouseEvent e) {
        taskDropped.accept(new DragAndDropTrio<>(original, task, target));
    }
}
