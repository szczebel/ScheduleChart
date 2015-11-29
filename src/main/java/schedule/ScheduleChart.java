package schedule;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

public class ScheduleChart<R extends Resource, E extends Event> implements ScheduleModel.Listener {
    final ScheduleModel<R, E> model;
    final ZonedDateTime start;
    final ZonedDateTime end;
    int rowHeight = 12;
    int pixelsPerHour = 2;
    int rowMargin = 2;
    EventRenderer<E> eventRenderer = new EventRenderer.Default<>();
    ResourceRenderer<R> resourceRenderer = new ResourceRenderer.Default<>();

    private JScrollPane scrollPane;
    private ChartPanel chartPanel;
    private ResourcePanel resourcePanel;
    private TimeLinePanel timeLinePanel;

    public ScheduleChart(ScheduleModel<R, E> scheduleModel, ZonedDateTime start, ZonedDateTime end) {
        model = scheduleModel;
        this.start = start;
        this.end = end;
        buildComponents();
        model.setListener(this);
    }


    private void buildComponents() {
        chartPanel = new ChartPanel();
        resourcePanel = new ResourcePanel();
        timeLinePanel = new TimeLinePanel();

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

        getComponent().repaint();
    }

    private int chartWidth() {
        return timeToX(end);
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
        return (int) Duration.between(start, time).toHours() * pixelsPerHour;
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
        this.eventRenderer = eventRenderer;
        getComponent().repaint();
    }

    public void setResourceRenderer(ResourceRenderer<R> resourceRenderer) {
        this.resourceRenderer = resourceRenderer;
        getComponent().repaint();
    }

    @Override
    public void dataChanged() {
        recalculateSizes();
    }


    private class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            List<R> resources = model.getResources();
            for (int i = 0; i < resources.size(); i++) {
                renderRow(g, i, resources.get(i));
            }
        }

        private void renderRow(Graphics g, int rowNumber, R resource) {
            renderRowBackground(g, rowNumber);
            int y = rowNumber * (rowHeight + 2 * rowMargin);
            model.getEventsAssignedTo(resource).forEach(event -> renderEvent(g, event, y + rowMargin));
        }

        private void renderRowBackground(Graphics g, int rowNumber) {
            ScheduleChart.this.renderRowBackground(g, rowNumber, getWidth());
        }


        private void renderEvent(Graphics g, E event, int y) {
            int x = timeToX(event.getStart());
            int width = timeToX(event.getEnd()) - x;
            Component renderingComponent = eventRenderer.getRenderingComponent(event);
            renderingComponent.setSize(new Dimension(width, rowHeight));
            renderingComponent.paint(g.create(x, y, width, rowHeight));

        }
    }

    private class TimeLinePanel extends JPanel {

    }

    private class ResourcePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            List<R> resources = model.getResources();
            for (int i = 0; i < resources.size(); i++) {
                renderRow(g, i, resources.get(i));
            }
        }

        private void renderRow(Graphics g, int rowNumber, R resource) {
            renderRowBackground(g, rowNumber);
            int y = rowNumber * (rowHeight + 2 * rowMargin) + rowMargin;
            Component renderingComponent = resourceRenderer.getRenderingComponent(resource);
            renderingComponent.setSize(new Dimension(getWidth(), rowHeight));
            renderingComponent.paint(g.create(0, y, getWidth(), rowHeight));

        }

        private void renderRowBackground(Graphics g, int rowNumber) {
            ScheduleChart.this.renderRowBackground(g, rowNumber, getWidth());
        }
    }

    private void renderRowBackground(Graphics g, int rowNumber, int width) {
        int y = rowNumber * (rowHeight + 2 * rowMargin);
        g.setColor(rowNumber % 2 == 0 ? Color.white : Color.decode("0xf9f9f9"));
        g.fillRect(0, y, width, rowHeight + 2 * rowMargin);
    }
}
