package schedule.model;

import java.time.ZonedDateTime;

public interface Event {

    ZonedDateTime getStart();

    ZonedDateTime getEnd();
}
