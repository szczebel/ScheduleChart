package schedule.model;

public abstract class AbstractScheduleModel<R extends Resource, TaskType extends Task> implements ScheduleModel<R, TaskType> {

    private ChangeListener changeListener = (a, b, c) -> {};

    protected void fireDataChanged(boolean resourcesChanged, boolean tasksChanged, boolean intervalChanged) {
        changeListener.dataChanged(resourcesChanged, tasksChanged, intervalChanged);
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

}
