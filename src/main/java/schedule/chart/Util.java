package schedule.chart;

import java.awt.*;

public class Util {
    public static void renderRowBackground(Graphics g, int rowNumber, int width, int rowHeight, int rowMargin) {
        int y = rowNumber * (rowHeight + 2 * rowMargin);
        g.setColor(rowNumber % 2 == 0 ? Color.white : Color.decode("0xf9f9f9"));
        g.fillRect(0, y, width, rowHeight + 2 * rowMargin);
    }
}
