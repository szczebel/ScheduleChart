package schedule.interaction;

import schedule.chart.TaskRenderer;
import schedule.model.Resource;
import schedule.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Tooltips<R extends Resource, TaskType extends Task> extends MouseInteractions.Default<R, TaskType> {
    final TaskRenderer<TaskType> renderer;

    private TaskType currentlyShowing;
    private Popup currentPopup;

    public Tooltips(TaskRenderer<TaskType> renderer) {
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
        if (currentPopup != null) currentPopup.hide();
        currentPopup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), renderingComponent, e.getXOnScreen(), e.getYOnScreen() - renderingComponent.getPreferredSize().height);
        currentPopup.show();
    }

    @Override
    public void mouseOverRow(R resource, MouseEvent e) {
        if (currentPopup != null) currentPopup.hide();
        currentPopup = null;
        currentlyShowing = null;
    }

    public static <R extends Resource, TaskType extends Task> Tooltips<R, TaskType> renderWith(TaskRenderer<TaskType> renderer) {
        return new Tooltips<>(renderer);
    }
}
