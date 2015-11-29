package schedule.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BasicScheduleModel<R extends Resource, E extends Event> implements ScheduleModel<R, E> {
    final Multimap<R, E> assignments = HashMultimap.create();
    final List<R> resources = new ArrayList<>();
    Listener listener = () -> {
    };
    private ZonedDateTime end = ZonedDateTime.now();
    private ZonedDateTime start = ZonedDateTime.now().minusDays(60);

    public void addResources(Collection<R> resources) {
        this.resources.addAll(resources);
        listener.dataChanged();
    }

    public void assign(R res, E event) {
        if (!resources.contains(res)) resources.add(res);
        assignments.put(res, event);
        start = earliestEvent().getStart().minusHours(1);
        end = latestEvent().getEnd().plusHours(1);
        listener.dataChanged();
    }

    private E earliestEvent() {
        E earliest = null;
        for (R r : assignments.keySet()) {
            for (E e : assignments.get(r)) {
                if (earliest == null || e.getStart().isBefore(earliest.getStart())) earliest = e;
            }
        }
        return earliest;
    }

    private E latestEvent() {
        E latest = null;
        for (R r : assignments.keySet()) {
            for (E e : assignments.get(r)) {
                if (latest == null || e.getEnd().isAfter(latest.getEnd())) latest = e;
            }
        }
        return latest;
    }

    public void clear() {
        resources.clear();
        assignments.clear();
        end = ZonedDateTime.now();
        start = ZonedDateTime.now().minusDays(60);
        listener.dataChanged();
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
    public Collection<E> getEventsAssignedTo(R resource) {
        return Collections.unmodifiableCollection(assignments.get(resource));
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
