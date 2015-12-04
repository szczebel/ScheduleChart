package schedule.view;

import schedule.model.Resource;
import schedule.model.ScheduleModel;
import schedule.model.Task;

import java.awt.*;
import java.util.*;
import java.util.List;

class ChartPanel<R extends Resource, TaskType extends Task> extends PanelWithRows {
    private final ScheduleModel<R, TaskType> model;
    TaskRenderer<TaskType> taskRenderer = new TaskRenderer.Default<>();
    private DraggedTask ghost = new DraggedTask();
    private Set<TaskType> selection = new HashSet<>();

    final Map<Rectangle, TaskType> hitMap = new HashMap<>();

    ChartPanel(RowHighlightTracker rowHighlightTracker, ScheduleModel<R, TaskType> model, ScheduleView.Configuration configuration) {
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
            renderRowTasks(g, i, resources.get(i));
        }
        renderDragged(g);
    }

    private void renderDragged(Graphics g) {
        if (ghost.isOn()) {
            NORMAL_RENDERING.render(g, ghost.task, getBounds(ghost.task, ghost.y));
        }
    }

    private void renderRowTasks(Graphics g, int rowNumber, R resource) {
        int y = rowNumber * configuration.getRowHeightWithMargins();
        model.getEventsAssignedTo(resource).forEach(event -> renderTask(g, event, y + configuration.rowMargin));
    }

    private void renderTask(Graphics g, TaskType task, int y) {
        Rectangle bounds = getBounds(task, y);
        if (ghost.task == task) PLACEHOLDER_RENDERING.render(g, task, bounds);
        else {
            NORMAL_RENDERING.render(g, task, bounds);
            renderSelectionDecoration(g, task, bounds);
        }
        hitMap.put(bounds, task);
    }

    private void renderSelectionDecoration(Graphics g, TaskType task, Rectangle bounds) {
        if (selection.contains(task)) {
            g.setColor(Color.red);
            g.drawRect(bounds.x - 1, bounds.y - 1, bounds.width + 1, bounds.height + 1);
            g.drawRect(bounds.x - 2, bounds.y - 2, bounds.width + 3, bounds.height + 3);
            g.drawRect(bounds.x - 3, bounds.y - 3, bounds.width + 5, bounds.height + 5);
        }
    }

    private Rectangle getBounds(TaskType task, int y) {
        int x = configuration.timeToX(task.getStart());
        int width = configuration.timeToX(task.getEnd()) - x;
        return new Rectangle(x, y, width, configuration.rowHeight);
    }

    TaskType getTaskAt(int x, int y) {
        Optional<Rectangle> possibleHit = hitMap.keySet().stream().filter(bounds -> withinBounds(bounds, x, y)).findFirst();
        if (possibleHit.isPresent()) {
            return hitMap.get(possibleHit.get());
        } else return null;
    }

    boolean withinBounds(Rectangle bounds, int x, int y) {
        return x > bounds.x && x < bounds.x + bounds.width && y > bounds.y && y < bounds.y + bounds.height;
    }

    void removeGhost() {
        ghost.hide();
        repaint();
    }

    void showGhost(TaskType task, int y) {
        ghost.show(task, y);
        repaint();
    }

    void clearSelection() {
        selection.clear();
        repaint();
    }

    void addSelected(TaskType task) {
        selection.add(task);
        repaint();
    }

    void setSelected(TaskType task) {
        selection.clear();
        addSelected(task);
    }

    Collection<TaskType> getSelection() {
        return Collections.unmodifiableCollection(selection);
    }

    class DraggedTask {
        TaskType task;
        int y;

        void show(TaskType task, int y) {
            this.task = task;
            this.y = y;
        }

        public void hide() {
            task = null;
        }

        public boolean isOn() {
            return task != null;
        }
    }

    interface TaskRenderingStrategy<TT extends Task> {
        void render(Graphics g, TT task, Rectangle bounds);
    }

    final TaskRenderingStrategy<TaskType> NORMAL_RENDERING = (g, task, bounds) -> {
        Component renderingComponent = taskRenderer.getRenderingComponent(task);
        renderingComponent.setSize(bounds.getSize());
        renderingComponent.doLayout();
        renderingComponent.paint(g.create(bounds.x, bounds.y, bounds.width, bounds.height));
    };

    final TaskRenderingStrategy<TaskType> PLACEHOLDER_RENDERING = (g, task, bounds) -> {
        g.setColor(Color.lightGray);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    };
}
