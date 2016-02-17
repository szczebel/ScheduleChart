package schedule.model;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

public interface ScheduleModel<R extends Resource, TaskType extends Task> {
    List<R> getResources();

    Collection<TaskType> getEventsAssignedTo(R resource);

    void setChangeListener(ChangeListener changeListener);

    ZonedDateTime getEnd();

    ZonedDateTime getStart();

    interface ChangeListener {
        void dataChanged(boolean resourcesChanged, boolean tasksChanged, boolean intervalChanged);
    }
}
