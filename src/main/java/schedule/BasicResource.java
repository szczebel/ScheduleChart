package schedule;

class BasicResource implements Resource {
    final String name;

    BasicResource(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
