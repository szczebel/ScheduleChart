package schedule;

import java.time.ZonedDateTime;

class BasicEvent implements Event {
    final ZonedDateTime start, end;
    final String name;

    BasicEvent(ZonedDateTime start, ZonedDateTime end, String name) {
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
