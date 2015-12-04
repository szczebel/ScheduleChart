package schedule.basic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import schedule.interaction.ReassignWithDragAndDrop;
import schedule.model.Resource;
import schedule.model.ScheduleModel;
import schedule.model.Task;

import java.time.ZonedDateTime;
import java.util.*;

@SuppressWarnings("unused")
public class BasicScheduleModel<R extends Resource, TaskType extends Task> implements ScheduleModel<R, TaskType>, ReassignWithDragAndDrop.ReassignHandler<R, TaskType> {

    final Multimap<R, TaskType> assignments = HashMultimap.create();
    final Map<TaskType, R> reverseAssignments = new HashMap<>();
    final List<R> resources = new ArrayList<>();
    Listener listener = () -> {
    };
    private ZonedDateTime end = ZonedDateTime.now();
    private ZonedDateTime start = ZonedDateTime.now().minusDays(7);

    public void addResources(Collection<R> resources) {
        this.resources.addAll(resources);
        listener.dataChanged();
    }

    public void assign(R res, TaskType event) {
        if (!resources.contains(res)) resources.add(res);
        assignments.put(res, event);
        if (reverseAssignments.containsKey(event))
            throw new RuntimeException(event + " already has assignment in this model");
        reverseAssignments.put(event, res);
        start = earliestEvent().getStart().minusHours(1);
        end = latestEvent().getEnd().plusHours(1);
        listener.dataChanged();
    }

    public void unassign(TaskType event) {
        removeFromMappings(event);
        start = earliestEvent().getStart().minusHours(1);
        end = latestEvent().getEnd().plusHours(1);
        listener.dataChanged();
    }

    public void clearAllData() {
        resources.clear();
        assignments.clear();
        reverseAssignments.clear();
        end = ZonedDateTime.now();
        start = ZonedDateTime.now().minusDays(60);
        listener.dataChanged();
    }

    protected TaskType earliestEvent() {
        TaskType earliest = null;
        for (R r : assignments.keySet()) {
            for (TaskType taskType : assignments.get(r)) {
                if (earliest == null || taskType.getStart().isBefore(earliest.getStart())) earliest = taskType;
            }
        }
        return earliest;
    }

    protected TaskType latestEvent() {
        TaskType latest = null;
        for (R r : assignments.keySet()) {
            for (TaskType taskType : assignments.get(r)) {
                if (latest == null || taskType.getEnd().isAfter(latest.getEnd())) latest = taskType;
            }
        }
        return latest;
    }

    @Override
    public ZonedDateTime getEnd() {
        return end;
    }

    @Override
    public ZonedDateTime getStart() {
        return start;
    }

    @Override
    public List<R> getResources() {
        return Collections.unmodifiableList(resources);
    }

    @Override
    public Collection<TaskType> getEventsAssignedTo(R resource) {
        return Collections.unmodifiableCollection(assignments.get(resource));
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void reassign(TaskType task, R newResource) {
        removeFromMappings(task);
        assign(newResource, task);
    }

    protected void removeFromMappings(TaskType task) {
        R r = reverseAssignments.get(task);
        if (r == null) throw new RuntimeException("Unknown task " + task);
        reverseAssignments.remove(task);
        assignments.remove(r, task);
    }
}
