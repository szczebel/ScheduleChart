package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MenuOnRightClick<R extends Resource, TaskType extends Task> extends EmptyInteractions<R, TaskType> {

    final Map<String, Consumer<TaskType>> taskMenuActions = new HashMap<>();
    final Map<String, Consumer<R>> rowMenuActions = new HashMap<>();

    public MenuOnRightClick<R, TaskType> addTaskMenuAction(String actionLabel, Consumer<TaskType> action) {
        taskMenuActions.put(actionLabel, action);
        return this;
    }

    public MenuOnRightClick<R, TaskType> addResourceMenuAction(String actionLabel, Consumer<R> action) {
        rowMenuActions.put(actionLabel, action);
        return this;
    }

    @Override
    public void mouseClickedOnRow(R resource, MouseEvent e) {
        if (e.getButton() == 3) {
            JPopupMenu popupMenu = new JPopupMenu("Actions");
            rowMenuActions.entrySet().forEach(p -> popupMenu.add(createRowMenuItem(p.getKey(), p.getValue(), resource)));
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseClickedOnTask(TaskType task, MouseEvent e) {
        if (e.getButton() == 3) {
            JPopupMenu popupMenu = new JPopupMenu("Actions");
            taskMenuActions.entrySet().forEach(p -> popupMenu.add(createTaskMenuItem(p.getKey(), p.getValue(), task)));
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    protected JMenuItem createTaskMenuItem(final String label, final Consumer<TaskType> action, final TaskType task) {
        return new JMenuItem(new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.accept(task);
            }
        });
    }

    protected JMenuItem createRowMenuItem(final String label, final Consumer<R> action, final R task) {
        return new JMenuItem(new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.accept(task);
            }
        });
    }

    public static <R extends Resource, TaskType extends Task> MenuOnRightClick<R, TaskType> create() {
        return new MenuOnRightClick<>();
    }
}
