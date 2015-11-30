package schedule.view;

import schedule.model.Resource;
import schedule.model.ScheduleModel;
import schedule.model.Task;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class ChartPanel<R extends Resource, TaskType extends Task> extends PanelWithRows {
    private final ScheduleModel<R, TaskType> model;
    TaskRenderer<TaskType> taskRenderer = new TaskRenderer.Default<>();

    final Map<Rectangle, TaskType> hitMap = new HashMap<>();

    public ChartPanel(RowHighlightTracker rowHighlightTracker, ScheduleModel<R, TaskType> model, ScheduleView.Configuration configuration) {
        super(configuration, rowHighlightTracker);
        this.model = model;
    }


    @Override
    protected void paintComponent(Graphics g) {
        List<R> resources = model.getResources();
        for (int i = 0; i < resources.size(); i++) {
            renderRowBackground(g, i);
        }
        Util.renderDayLines(g, getHeight(), null, configuration, model.getStart(), model.getEnd());

        hitMap.clear();
        for (int i = 0; i < resources.size(); i++) {
            renderRowEvents(g, i, resources.get(i));
        }
    }

    private void renderRowEvents(Graphics g, int rowNumber, R resource) {
        int y = rowNumber * configuration.getRowHeightWithMargins();
        model.getEventsAssignedTo(resource).forEach(event -> renderEvent(g, event, y + configuration.rowMargin));
    }


    private void renderEvent(Graphics g, TaskType task, int y) {
        int x = configuration.timeToX(task.getStart());
        int width = configuration.timeToX(task.getEnd()) - x;
        Component renderingComponent = taskRenderer.getRenderingComponent(task);
        renderingComponent.setSize(new Dimension(width, configuration.rowHeight));
        renderingComponent.doLayout();
        renderingComponent.paint(g.create(x, y, width, configuration.rowHeight));
        hitMap.put(new Rectangle(x, y, width, configuration.rowHeight), task);
    }

    public TaskType getTaskAt(int x, int y) {
        Optional<Rectangle> possibleHit = hitMap.keySet().stream().filter(bounds -> withinBounds(bounds, x, y)).findFirst();
        if (possibleHit.isPresent()) {
            return hitMap.get(possibleHit.get());
        } else return null;
    }

    boolean withinBounds(Rectangle bounds, int x, int y) {
        return x > bounds.x && x < bounds.x + bounds.width && y > bounds.y && y < bounds.y + bounds.height;
    }
}
