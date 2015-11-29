package schedule.chart;

import schedule.model.Event;
import schedule.model.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class ChartPanel<R extends Resource, E extends Event> extends JPanel {
    private ScheduleChart<R, E> scheduleChart;
    EventRenderer<E> eventRenderer = new EventRenderer.Default<>();


    public ChartPanel(ScheduleChart<R, E> scheduleChart) {
        this.scheduleChart = scheduleChart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        List<R> resources = scheduleChart.model.getResources();
        for (int i = 0; i < resources.size(); i++) {
            renderRow(g, i, resources.get(i));
        }
    }

    private void renderRow(Graphics g, int rowNumber, R resource) {
        renderRowBackground(g, rowNumber);
        int y = rowNumber * (scheduleChart.rowHeight + 2 * scheduleChart.rowMargin);
        scheduleChart.model.getEventsAssignedTo(resource).forEach(event -> renderEvent(g, event, y + scheduleChart.rowMargin));
    }

    private void renderRowBackground(Graphics g, int rowNumber) {
        Util.renderRowBackground(g, rowNumber, getWidth(), scheduleChart.rowHeight, scheduleChart.rowMargin);
    }


    private void renderEvent(Graphics g, E event, int y) {
        int x = scheduleChart.timeToX(event.getStart());
        int width = scheduleChart.timeToX(event.getEnd()) - x;
        Component renderingComponent = eventRenderer.getRenderingComponent(event);
        renderingComponent.setSize(new Dimension(width, scheduleChart.rowHeight));
        renderingComponent.paint(g.create(x, y, width, scheduleChart.rowHeight));

    }
}
