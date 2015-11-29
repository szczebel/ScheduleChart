package schedule.model;

import java.time.ZonedDateTime;

public interface Task {

    ZonedDateTime getStart();

    ZonedDateTime getEnd();
}
