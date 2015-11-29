package schedule.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BasicScheduleModel<R extends Resource, E extends Event> implements ScheduleModel<R, E> {
    final Multimap<R, E> assignments = HashMultimap.create();
    final List<R> resources = new ArrayList<>();
    Listener listener = () -> {
    };

    public void addResources(Collection<R> resources) {
        this.resources.addAll(resources);
        listener.dataChanged();
    }

    public void assign(R res, E event) {
        if (!resources.contains(res)) resources.add(res);
        assignments.put(res, event);
        listener.dataChanged();
    }

    public void assign(Multimap<R, E> assignments) {
        resources.addAll(assignments.keySet());
        this.assignments.putAll(assignments);
        listener.dataChanged();
    }

    public void clear() {
        resources.clear();
        assignments.clear();
        listener.dataChanged();
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
