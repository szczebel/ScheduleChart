package schedule.view;

import schedule.interaction.EmptyInteractions;
import schedule.interaction.MouseInteractions;
import schedule.model.Resource;
import schedule.model.ScheduleModel;
import schedule.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;

@SuppressWarnings("unused")
public class ScheduleView<R extends Resource, TaskType extends Task> implements ScheduleModel.Listener {
    private static final int MAX_PIXELS_PER_HOUR = 60;
    final ScheduleModel<R, TaskType> model;
    final Configuration configuration = new Configuration();

    private JScrollPane scrollPane;
    private ChartPanel<R, TaskType> chartPanel;
    private ResourcePanel<R> resourcePanel;
    private TimeLinePanel timeLinePanel;
    private boolean dragAndDropEnabled;

    MouseInteractions<R, TaskType> mouseInteractions = new EmptyInteractions<>();

    public ScheduleView(ScheduleModel<R, TaskType> scheduleModel) {
        model = scheduleModel;
        buildComponents();
        model.setListener(this);
    }

    private void buildComponents() {
        RowHighlightTracker rowHighlightTracker = new RowHighlightTracker(this);
        chartPanel = new ChartPanel<>(rowHighlightTracker, model, configuration);
        resourcePanel = new ResourcePanel<>(rowHighlightTracker, model, configuration);
        timeLinePanel = new TimeLinePanel(model, this.configuration);

        wireInteractions();

        scrollPane = new JScrollPane(chartPanel) { //adding MWheelListener to the scrollpane didn't work correctly
            @Override
            protected void processMouseWheelEvent(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() > 0) zoomIn();
                    if (e.getWheelRotation() < 0) zoomOut();
                } else super.processMouseWheelEvent(e);
            }
        };
        scrollPane.setColumnHeaderView(timeLinePanel);
        scrollPane.setRowHeaderView(resourcePanel);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        recalculateSizes();
    }


    private void wireInteractions() {
        MouseAdapter interactionInvoker = new InteractionInvoker();
        chartPanel.addMouseMotionListener(interactionInvoker);
        chartPanel.addMouseListener(interactionInvoker);
    }

    ResourceAndTask<R, TaskType> getResourceAndTask(int x, int y) {
        ResourceAndTask<R, TaskType> retval = new ResourceAndTask<>(null, null);
        int rowNumber = y / configuration.getRowHeightWithMargins();
        if (rowNumber >= 0 && rowNumber < model.getResources().size()) {
            retval.resource = model.getResources().get(rowNumber);
            retval.task = chartPanel.getTaskAt(x, y);
        }
        return retval;
    }

    private void recalculateSizes() {
        chartPanel.setPreferredSize(new Dimension(chartWidth(), chartHeight()));
        resourcePanel.setPreferredSize(new Dimension(200, chartHeight()));
        timeLinePanel.setPreferredSize(new Dimension(chartWidth(), 30));
        scrollPane.getVerticalScrollBar().setUnitIncrement(configuration.getRowHeightWithMargins());
        chartPanel.revalidate();
        getComponent().repaint();
    }

    private int chartWidth() {
        return configuration.timeToX(model.getEnd());
    }

    private int chartHeight() {
        return model.getResources().size() * configuration.getRowHeightWithMargins();
    }

    public void zoomIn() {
        setPixelsPerHour(configuration.pixelsPerHour * 2);
    }

    public void zoomOut() {
        setPixelsPerHour(configuration.pixelsPerHour / 2);
    }

    public JComponent getComponent() {
        return scrollPane;
    }

    public void enableDragAndDrop(boolean enable) {
        this.dragAndDropEnabled = enable;
    }

    public <I extends MouseInteractions<R, TaskType>> void setMouseInteractions(I mouseInteractions) {
        this.mouseInteractions = mouseInteractions;
    }

    public void setRowHeight(int rowHeight) {
        configuration.rowHeight = rowHeight;
        recalculateSizes();
    }

    private void setPixelsPerHour(int pixelsPerHour) {
        configuration.pixelsPerHour = Math.min(Math.max(1, pixelsPerHour), MAX_PIXELS_PER_HOUR);
        recalculateSizes();
    }

    public void setRowMargin(int rowMargin) {
        configuration.rowMargin = rowMargin;
        recalculateSizes();
    }

    public void setTaskRenderer(TaskRenderer<TaskType> taskRenderer) {
        chartPanel.taskRenderer = taskRenderer;
        getComponent().repaint();
    }

    public void setResourceRenderer(ResourceRenderer<R> resourceRenderer) {
        resourcePanel.resourceRenderer = resourceRenderer;
        getComponent().repaint();
    }

    public Collection<TaskType> getSelection() {
        return chartPanel.getSelection();
    }


    @Override
    public void dataChanged(boolean resourcesChanged, boolean tasksChanged, boolean intervalChanged) {
        if (resourcesChanged || intervalChanged) recalculateSizes();
        else chartPanel.repaint();
    }

    public class Configuration {
        int rowHeight = 14;
        int pixelsPerHour = 2;
        int rowMargin = 2;

        int getRowHeightWithMargins() {
            return rowHeight + 2 * rowMargin;
        }

        int timeToX(ZonedDateTime time) {
            return (int) Duration.between(model.getStart(), time).toHours() * configuration.pixelsPerHour;
        }

    }

    static class ResourceAndTask<R extends Resource, TaskType extends Task> {
        R resource;
        TaskType task;

        public ResourceAndTask(R resource, TaskType task) {
            this.resource = resource;
            this.task = task;
        }

        public boolean hasBoth() {
            return resource != null && task != null;
        }

        public boolean onlyResource() {
            return resource != null && task == null;
        }
    }

    private class InteractionInvoker extends MouseAdapter {
        ResourceAndTask<R, TaskType> dragSource;

        @Override
        public void mousePressed(MouseEvent e) {
            if (!dragAndDropEnabled) return;
            dragSource = getResourceAndTask(e.getX(), e.getY());
            if (dragSource.hasBoth()) chartPanel.showGhost(dragSource.task, e.getY() - configuration.rowHeight / 2);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!dragAndDropEnabled) return;
            if (!dragSource.hasBoth()) return;
            chartPanel.showGhost(dragSource.task, e.getY() - configuration.rowHeight / 2);
            ResourceAndTask<R, TaskType> intermediateTarget = getResourceAndTask(e.getX(), e.getY());
            if (intermediateTarget.resource != null) {
                mouseInteractions.taskDraggedOverRow(dragSource.resource, dragSource.task, intermediateTarget.resource, e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!dragAndDropEnabled) return;
            if (!dragSource.hasBoth()) return;
            chartPanel.removeGhost();
            ResourceAndTask<R, TaskType> target = getResourceAndTask(e.getX(), e.getY());
            if (target.resource != null && dragSource.resource != target.resource) {
                mouseInteractions.taskDroppedOnRow(dragSource.resource, dragSource.task, target.resource, e);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            ResourceAndTask<R, TaskType> resourceAndTask = getResourceAndTask(e.getX(), e.getY());
            if (resourceAndTask.hasBoth()) {
                mouseInteractions.mouseOverTask(resourceAndTask.task, e);
            } else if (resourceAndTask.onlyResource()) {
                mouseInteractions.mouseOverEmptySpace(resourceAndTask.resource, e);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            ResourceAndTask<R, TaskType> resourceAndTask = getResourceAndTask(e.getX(), e.getY());
            if (resourceAndTask.hasBoth()) {
                if (e.isControlDown()) chartPanel.addSelected(resourceAndTask.task);
                else chartPanel.setSelected(resourceAndTask.task);
                mouseInteractions.mouseClickedOnTask(resourceAndTask.task, e);
            } else if (resourceAndTask.onlyResource()) {
                chartPanel.clearSelection();
                mouseInteractions.mouseClickedOnRow(resourceAndTask.resource, e);
            }
        }
    }
}
