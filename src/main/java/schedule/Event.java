package schedule;

import java.time.ZonedDateTime;

interface Event {

    ZonedDateTime getStart();

    ZonedDateTime getEnd();
}
