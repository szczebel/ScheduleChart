package schedule.interaction;

import schedule.model.Resource;
import schedule.model.Task;
import schedule.view.TaskRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class InstantTooltips<R extends Resource, TaskType extends Task> extends MouseInteractions.Default<R, TaskType> {
    final TaskRenderer<TaskType> renderer;

    private TaskType currentlyShowing;
    private JPopupMenu currentPopup;

    InstantTooltips(TaskRenderer<TaskType> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void mouseOverTask(TaskType task, MouseEvent e) {
        if (currentlyShowing == task) return;
        currentlyShowing = task;
        JComponent renderingComponent = renderer.getRenderingComponent(task);
        LayoutManager layout = renderingComponent.getLayout();
        if (layout != null) {
            Dimension dimension = layout.preferredLayoutSize(renderingComponent);
            renderingComponent.setPreferredSize(dimension);
        }
        if (currentPopup != null) currentPopup.setVisible(false);
        currentPopup = new JPopupMenu();
        currentPopup.setLayout(new BorderLayout());
        currentPopup.add(renderingComponent);
        currentPopup.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void mouseOverEmptySpace(R resource, MouseEvent e) {
        reset();
    }

    @Override
    public void taskDraggedOverRow(TaskType task, R resource, MouseEvent e) {
        reset();
    }

    void reset() {
        if (currentPopup != null) currentPopup.setVisible(false);
        currentPopup = null;
        currentlyShowing = null;
    }

    public static <R extends Resource, TaskType extends Task> InstantTooltips<R, TaskType> renderWith(TaskRenderer<TaskType> renderer) {
        return new InstantTooltips<>(renderer);
    }
}
