package schedule.chart;

import schedule.model.ScheduleModel;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

class TimeLinePanel extends JPanel {

    private ScheduleModel model;
    private ScheduleChart.Configuration configuration;

    public TimeLinePanel(ScheduleModel model, ScheduleChart.Configuration configuration) {
        this.model = model;
        this.configuration = configuration;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Util.renderDayLines(g, getHeight(), DateTimeFormatter.ofPattern("dd.MMM"), configuration, model.getStart(), model.getEnd());


    }

}
