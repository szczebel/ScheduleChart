package schedule.demo.meetingroom;

import schedule.model.Task;

import java.time.ZonedDateTime;

class Meeting implements Task {
    final ZonedDateTime start, end;
    final String name;

    public Meeting(ZonedDateTime start, ZonedDateTime end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return name;
    }
}
