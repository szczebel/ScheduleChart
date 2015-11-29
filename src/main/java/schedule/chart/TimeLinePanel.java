package schedule.chart;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

class TimeLinePanel extends JPanel {

    private ScheduleChart scheduleChart;

    public TimeLinePanel(ScheduleChart scheduleChart) {
        this.scheduleChart = scheduleChart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Util.renderDayLines(g, scheduleChart, getHeight(), DateTimeFormatter.ofPattern("dd.MMM"));


    }

}
