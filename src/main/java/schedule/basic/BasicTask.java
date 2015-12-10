package schedule.basic;

import schedule.model.Task;

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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
