package schedule.model;

public class BasicResource implements Resource {
    final String name;

    public BasicResource(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
