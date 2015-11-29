package schedule;

import java.util.Collection;
import java.util.List;

interface ScheduleModel<R extends Resource, E extends Event> {
    List<R> getResources();

    Collection<E> getEventsAssignedTo(R resource);

    interface Listener {
        void dataChanged();
    }
}
