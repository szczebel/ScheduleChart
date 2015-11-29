package schedule;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class BasicScheduleModel<R extends Resource, E extends Event> implements ScheduleModel<R, E> {
    final Multimap<R, E> assignments = HashMultimap.create();
    final List<R> resources = new ArrayList<>();

    public void register(Collection<R> resources) {
        this.resources.addAll(resources);
    }

    public void assign(R res, E event) {
        if (!resources.contains(res)) throw new IllegalArgumentException("Unknown resource: " + res);
        assignments.put(res, event);
    }

    @Override
    public List<R> getResources() {
        return Collections.unmodifiableList(resources);
    }

    @Override
    public Collection<E> getEventsAssignedTo(R resource) {
        return Collections.unmodifiableCollection(assignments.get(resource));
    }
}
