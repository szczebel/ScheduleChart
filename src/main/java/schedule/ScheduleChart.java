package schedule;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

public class ScheduleChart<R extends Resource, E extends Event> {
    final ScheduleModel<R, E> model;
    final ZonedDateTime start;
    final ZonedDateTime end;
    int rowHeight = 20;
    int pixelsPerHour = 6;
    int rowMargin = 2;
    EventRenderer<E> eventRenderer = new EventRenderer.Default<>();
    ResourceRenderer<R> resourceRenderer = new ResourceRenderer.Default<>();

    private final JComponent component;

    public ScheduleChart(ScheduleModel<R, E> scheduleModel, ZonedDateTime start, ZonedDateTime end) {
        model = scheduleModel;
        this.start = start;
        this.end = end;
        component = buildComponent();
    }


    private JComponent buildComponent() {
        JPanel panel = new ChartPanel();
        panel.setPreferredSize(new Dimension(chartWidth(), chartHeight()));

        JScrollPane scrollPane = new JScrollPane(panel);

        scrollPane.setColumnHeaderView(createTimeline());
        scrollPane.setRowHeaderView(createResourceList());

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(rowHeight + 2 * rowMargin);
        return scrollPane;
    }

    private int chartWidth() {
        return timeToX(end);
    }

    private int chartHeight() {
        return model.getResources().size() * (rowHeight + 2 * rowMargin);
    }

    private Component createResourceList() {
        JPanel panel = new ResourcePanel();
        panel.setPreferredSize(new Dimension(200, chartHeight()));
        return panel;
    }

    private Component createTimeline() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(chartWidth(), 30));
        panel.setBackground(Color.cyan);
        return panel;
    }

    int timeToX(ZonedDateTime time) {
        return (int) Duration.between(start, time).toHours() * pixelsPerHour;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public void setPixelsPerHour(int pixelsPerHour) {
        this.pixelsPerHour = pixelsPerHour;
    }

    public void setRowMargin(int rowMargin) {
        this.rowMargin = rowMargin;
    }

    public void setEventRenderer(EventRenderer<E> eventRenderer) {
        this.eventRenderer = eventRenderer;
    }

    public void setResourceRenderer(ResourceRenderer<R> resourceRenderer) {
        this.resourceRenderer = resourceRenderer;
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
