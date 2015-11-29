package schedule.chart;

import javax.swing.*;
import java.awt.*;

class PanelWithRows extends JPanel {

    RowHighlightTracker rowHighlightTracker;
    ScheduleChart.Configuration configuration;

    PanelWithRows(ScheduleChart.Configuration configuration, RowHighlightTracker rowHighlightTracker) {
        this.configuration = configuration;
        this.rowHighlightTracker = rowHighlightTracker;
        addMouseMotionListener(rowHighlightTracker);
        addMouseListener(rowHighlightTracker);

    }

    protected void renderRowBackground(Graphics g, int rowNumber) {
        int y = rowNumber * (configuration.rowHeight + 2 * configuration.rowMargin);
        g.setColor(rowNumber % 2 == 0 ? Color.white : Color.decode("0xf9f9f9"));
        if (rowNumber == rowHighlightTracker.mouseOverRow) g.setColor(Color.yellow);
        g.fillRect(0, y, getWidth(), configuration.rowHeight + 2 * configuration.rowMargin);
    }
}
