package schedule.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class RowHighlightTracker extends MouseAdapter {
    private ScheduleView scheduleView;
    int mouseOverRow = -1;

    public RowHighlightTracker(ScheduleView scheduleView) {
        this.scheduleView = scheduleView;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        handleMove(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handleMove(e);
    }

    protected void handleMove(MouseEvent e) {
        mouseOverRow = e.getY() / scheduleView.configuration.getRowHeightWithMargins();
        scheduleView.getComponent().repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOverRow = -1;
        scheduleView.getComponent().repaint();
    }
}
