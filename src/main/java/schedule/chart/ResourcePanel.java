package schedule.chart;

import schedule.model.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class ResourcePanel<R extends Resource> extends JPanel {
    private ScheduleChart<R, ?> scheduleChart;
    ResourceRenderer<R> resourceRenderer = new ResourceRenderer.Default<>();

    public ResourcePanel(ScheduleChart<R, ?> scheduleChart) {
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
        int y = rowNumber * (scheduleChart.rowHeight + 2 * scheduleChart.rowMargin) + scheduleChart.rowMargin;
        Component renderingComponent = resourceRenderer.getRenderingComponent(resource);
        renderingComponent.setSize(new Dimension(getWidth(), scheduleChart.rowHeight));
        renderingComponent.paint(g.create(0, y, getWidth(), scheduleChart.rowHeight));

    }

    private void renderRowBackground(Graphics g, int rowNumber) {
        Util.renderRowBackground(g, rowNumber, getWidth(), scheduleChart.rowHeight, scheduleChart.rowMargin);
    }
}
