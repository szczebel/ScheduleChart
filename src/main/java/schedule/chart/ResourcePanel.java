package schedule.chart;

import schedule.model.Resource;

import java.awt.*;
import java.util.List;

class ResourcePanel<R extends Resource> extends PanelWithRows {
    ResourceRenderer<R> resourceRenderer = new ResourceRenderer.Default<>();
    final ScheduleChart<R, ?> scheduleChart;

    public ResourcePanel(ScheduleChart<R, ?> scheduleChart, RowHighlightTracker rowHighlightTracker) {
        super(scheduleChart.configuration, rowHighlightTracker);
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
        Component renderingComponent = resourceRenderer.getRenderingComponent(resource);
        int y = rowNumber * configuration.getRowHeightWithMargins() + configuration.rowMargin;
        renderingComponent.setSize(new Dimension(getWidth(), configuration.rowHeight));
        renderingComponent.paint(g.create(0, y, getWidth(), configuration.rowHeight));

    }
}
