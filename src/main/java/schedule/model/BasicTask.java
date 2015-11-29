package schedule.model;

import java.time.ZonedDateTime;

public class BasicTask implements Task {
    final ZonedDateTime start, end;
    final String name;

    public BasicTask(ZonedDateTime start, ZonedDateTime end, String name) {
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
