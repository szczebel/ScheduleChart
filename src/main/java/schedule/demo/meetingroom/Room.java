package schedule.demo.meetingroom;

import schedule.model.Resource;

class Room implements Resource {
    final String name;

    Room(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
