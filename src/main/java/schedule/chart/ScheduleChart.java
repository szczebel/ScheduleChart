package schedule.chart;

import schedule.model.Resource;
import schedule.model.ScheduleModel;
import schedule.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.ZonedDateTime;

@SuppressWarnings("unused")
public class ScheduleChart<R extends Resource, TaskType extends Task> implements ScheduleModel.Listener {
    private static final int MAX_PIXELS_PER_HOUR = 60;
    final ScheduleModel<R, TaskType> model;
    final Configuration configuration = new Configuration();

    private JScrollPane scrollPane;
    private ChartPanel chartPanel;
    private ResourcePanel resourcePanel;
    private TimeLinePanel timeLinePanel;

    public ScheduleChart(ScheduleModel<R, TaskType> scheduleModel) {
        model = scheduleModel;
        buildComponents();
        model.setListener(this);
    }


    private void buildComponents() {
        RowHighlightTracker rowHighlightTracker = new RowHighlightTracker(this);
        chartPanel = new ChartPanel<>(this, rowHighlightTracker);
        resourcePanel = new ResourcePanel<>(this, rowHighlightTracker);
        timeLinePanel = new TimeLinePanel(this);

        scrollPane = new JScrollPane(chartPanel);
        scrollPane.setColumnHeaderView(timeLinePanel);
        scrollPane.setRowHeaderView(resourcePanel);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        recalculateSizes();

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
        return timeToX(model.getEnd());
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

    int timeToX(ZonedDateTime time) {
        return (int) Duration.between(model.getStart(), time).toHours() * configuration.pixelsPerHour;
    }

    public JComponent getComponent() {
        return scrollPane;
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

    public void setEventRenderer(TaskRenderer<TaskType> taskRenderer) {
        chartPanel.taskRenderer = taskRenderer;
        getComponent().repaint();
    }

    public void setResourceRenderer(ResourceRenderer<R> resourceRenderer) {
        resourcePanel.resourceRenderer = resourceRenderer;
        getComponent().repaint();
    }

    @Override
    public void dataChanged() {
        recalculateSizes();
    }

    public static class Configuration {
        int rowHeight = 14;
        int pixelsPerHour = 2;
        int rowMargin = 2;

        int getRowHeightWithMargins() {
            return rowHeight + 2 * rowMargin;
        }
    }
}
