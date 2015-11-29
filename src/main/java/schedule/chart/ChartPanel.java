package schedule.chart;

import schedule.model.Event;
import schedule.model.Resource;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.List;

class ChartPanel<R extends Resource, E extends Event> extends PanelWithRows {
    final ScheduleChart<R, E> scheduleChart;
    EventRenderer<E> eventRenderer = new EventRenderer.Default<>();


    public ChartPanel(ScheduleChart<R, E> scheduleChart, RowHighlightTracker rowHighlightTracker) {
        super(scheduleChart.configuration, rowHighlightTracker);
        this.scheduleChart = scheduleChart;

        addMouseWheelListener(this::onWheelEvent);
    }

    private void onWheelEvent(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() > 0) scheduleChart.zoomIn();
            if (e.getWheelRotation() < 0) scheduleChart.zoomOut();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        List<R> resources = scheduleChart.model.getResources();
        for (int i = 0; i < resources.size(); i++) {
            renderRowBackground(g, i);
        }
        Util.renderDayLines(g, scheduleChart, getHeight(), null);
        for (int i = 0; i < resources.size(); i++) {
            renderRowEvents(g, i, resources.get(i));
        }
    }

    private void renderRowEvents(Graphics g, int rowNumber, R resource) {
        int y = rowNumber * configuration.getRowHeightWithMargins();
        scheduleChart.model.getEventsAssignedTo(resource).forEach(event -> renderEvent(g, event, y + configuration.rowMargin));
    }


    private void renderEvent(Graphics g, E event, int y) {
        int x = scheduleChart.timeToX(event.getStart());
        int width = scheduleChart.timeToX(event.getEnd()) - x;
        Component renderingComponent = eventRenderer.getRenderingComponent(event);
        renderingComponent.setSize(new Dimension(width, configuration.rowHeight));
        renderingComponent.doLayout();
        renderingComponent.paint(g.create(x, y, width, configuration.rowHeight));

    }
}
