package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;

import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;

public interface MouseInteractions<R extends Resource, TaskType extends Task> {
    void mouseOverTask(TaskType task, MouseEvent e);

    void mouseClickedOnTask(TaskType task, MouseEvent e);

    void mouseClickedOnRow(R resource, MouseEvent e);

    void mouseOverEmptySpace(R resource, MouseEvent e);

    void taskDraggedOverRow(TaskType task, R resource, MouseEvent e);

    void taskDroppedOnRow(TaskType task, R resource, MouseEvent e);

    class Default<R extends Resource, TaskType extends Task> implements MouseInteractions<R, TaskType> {

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
        public void taskDraggedOverRow(TaskType task, R resource, MouseEvent e) {

        }

        @Override
        public void taskDroppedOnRow(TaskType task, R resource, MouseEvent e) {

        }
    }

    class Aggregate<R extends Resource, TaskType extends Task> implements MouseInteractions<R, TaskType> {

        private final Collection<MouseInteractions<R, TaskType>> allInteractions;

        public Aggregate(Collection<MouseInteractions<R, TaskType>> allInteractions) {
            this.allInteractions = allInteractions;
        }

        @Override
        public void mouseOverTask(TaskType task, MouseEvent e) {
            allInteractions.forEach(i -> i.mouseOverTask(task, e));
        }

        @Override
        public void mouseClickedOnTask(TaskType task, MouseEvent e) {
            allInteractions.forEach(i -> i.mouseClickedOnTask(task, e));
        }

        @Override
        public void mouseClickedOnRow(R resource, MouseEvent e) {
            allInteractions.forEach(i -> i.mouseClickedOnRow(resource, e));
        }

        @Override
        public void mouseOverEmptySpace(R resource, MouseEvent e) {
            allInteractions.forEach(i -> i.mouseOverEmptySpace(resource, e));
        }

        @Override
        public void taskDraggedOverRow(TaskType task, R resource, MouseEvent e) {
            allInteractions.forEach(i -> i.taskDraggedOverRow(task, resource, e));
        }

        @Override
        public void taskDroppedOnRow(TaskType task, R resource, MouseEvent e) {
            allInteractions.forEach(i -> i.taskDroppedOnRow(task, resource, e));
        }

        @SafeVarargs
        public static <R extends Resource, TaskType extends Task> Aggregate<R, TaskType> of(MouseInteractions<R, TaskType>... allInteractions) {
            return new Aggregate<>(Arrays.asList(allInteractions));
        }
    }
}
