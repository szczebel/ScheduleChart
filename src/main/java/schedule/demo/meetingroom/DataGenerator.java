package schedule.demo.meetingroom;

import schedule.model.GenericScheduleModel;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private final Random random = new Random();

    void generate(GenericScheduleModel<Room, Meeting> model, ZonedDateTime start, ZonedDateTime end) {
        List<Room> resources = new ArrayList<>();
        for (int i = 100; i <= 200; ++i) resources.add(new Room("Conference room " + i));
        resources.forEach(resource -> buildLineOfWork(resource, model, start, end));
    }

    int counter = 100;
    private void buildLineOfWork(Room room, GenericScheduleModel<Room, Meeting> model, ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime currentTime = start;
        while (currentTime.isBefore(end)) {
            ZonedDateTime meetingStart = step(currentTime);
            currentTime = step(meetingStart);
            Meeting meeting = new Meeting(meetingStart, currentTime, "Meeting # " + counter++);
            model.assign(room, meeting);
        }
    }

    private ZonedDateTime step(ZonedDateTime currentTime) {
        currentTime = currentTime.plusHours(6 + random.nextInt(72)).plusMinutes(random.nextInt(60));
        return currentTime;
    }
}
