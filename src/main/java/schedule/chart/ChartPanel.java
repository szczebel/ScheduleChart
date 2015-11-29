package schedule.chart;

import schedule.model.Resource;
import schedule.model.ScheduleModel;
import schedule.model.Task;

import java.awt.*;
import java.util.List;

class ChartPanel<R extends Resource, E extends Task> extends PanelWithRows {
    private final ScheduleModel<R, E> model;
    TaskRenderer<E> taskRenderer = new TaskRenderer.Default<>();


    public ChartPanel(RowHighlightTracker rowHighlightTracker, ScheduleModel<R, E> model, ScheduleChart.Configuration configuration) {
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
        for (int i = 0; i < resources.size(); i++) {
            renderRowEvents(g, i, resources.get(i));
        }
    }

    private void renderRowEvents(Graphics g, int rowNumber, R resource) {
        int y = rowNumber * configuration.getRowHeightWithMargins();
        model.getEventsAssignedTo(resource).forEach(event -> renderEvent(g, event, y + configuration.rowMargin));
    }


    private void renderEvent(Graphics g, E event, int y) {
        int x = configuration.timeToX(event.getStart());
        int width = configuration.timeToX(event.getEnd()) - x;
        Component renderingComponent = taskRenderer.getRenderingComponent(event);
        renderingComponent.setSize(new Dimension(width, configuration.rowHeight));
        renderingComponent.doLayout();
        renderingComponent.paint(g.create(x, y, width, configuration.rowHeight));

    }
}
