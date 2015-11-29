package schedule.chart;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class RowHighlightTracker extends MouseAdapter {
    private ScheduleChart scheduleChart;
    int mouseOverRow = -1;

    public RowHighlightTracker(ScheduleChart scheduleChart) {
        this.scheduleChart = scheduleChart;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseOverRow = e.getY() / scheduleChart.configuration.getRowHeightWithMargins();
        scheduleChart.getComponent().repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOverRow = -1;
        scheduleChart.getComponent().repaint();
    }
}
