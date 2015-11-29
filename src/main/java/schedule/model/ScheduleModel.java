package schedule.model;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

public interface ScheduleModel<R extends Resource, E extends Event> {
    List<R> getResources();

    Collection<E> getEventsAssignedTo(R resource);

    void setListener(Listener listener);

    ZonedDateTime getEnd();

    ZonedDateTime getStart();

    interface Listener {
        void dataChanged();
    }
}