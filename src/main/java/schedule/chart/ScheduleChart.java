package schedule.chart;

import schedule.model.Event;
import schedule.model.Resource;
import schedule.model.ScheduleModel;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.ZonedDateTime;

@SuppressWarnings("unused")
public class ScheduleChart<R extends Resource, E extends Event> implements ScheduleModel.Listener {
    final ScheduleModel<R, E> model;
    int rowHeight = 12;
    int pixelsPerHour = 2;
    int rowMargin = 2;

    private JScrollPane scrollPane;
    private ChartPanel chartPanel;
    private ResourcePanel resourcePanel;
    private TimeLinePanel timeLinePanel;

    public ScheduleChart(ScheduleModel<R, E> scheduleModel) {
        model = scheduleModel;
        buildComponents();
        model.setListener(this);
    }


    private void buildComponents() {
        chartPanel = new ChartPanel<>(this);
        resourcePanel = new ResourcePanel<>(this);
        timeLinePanel = new TimeLinePanel(this);

        scrollPane = new JScrollPane(chartPanel);
        scrollPane.setColumnHeaderView(timeLinePanel);
        scrollPane.setRowHeaderView(resourcePanel);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(rowHeight + 2 * rowMargin);

        recalculateSizes();

    }

    private void recalculateSizes() {
        chartPanel.setPreferredSize(new Dimension(chartWidth(), chartHeight()));
        resourcePanel.setPreferredSize(new Dimension(200, chartHeight()));
        timeLinePanel.setPreferredSize(new Dimension(chartWidth(), 30));
        chartPanel.revalidate();
        getComponent().repaint();
    }

    private int chartWidth() {
        return timeToX(model.getEnd());
    }

    private int chartHeight() {
        return model.getResources().size() * (rowHeight + 2 * rowMargin);
    }

    public void zoomIn() {
        setPixelsPerHour(pixelsPerHour * 2);
    }

    public void zoomOut() {
        setPixelsPerHour(pixelsPerHour / 2);
    }

    int timeToX(ZonedDateTime time) {
        return (int) Duration.between(model.getStart(), time).toHours() * pixelsPerHour;
    }

    public JComponent getComponent() {
        return scrollPane;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        recalculateSizes();
    }

    private void setPixelsPerHour(int pixelsPerHour) {
        this.pixelsPerHour = pixelsPerHour;
        if (pixelsPerHour < 1) this.pixelsPerHour = 1;
        recalculateSizes();
    }

    public void setRowMargin(int rowMargin) {
        this.rowMargin = rowMargin;
        recalculateSizes();
    }

    public void setEventRenderer(EventRenderer<E> eventRenderer) {
        chartPanel.eventRenderer = eventRenderer;
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
}
