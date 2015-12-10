package schedule.demo.simple;

import schedule.basic.BasicResource;
import schedule.basic.BasicTask;
import schedule.basic.GenericScheduleModel;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private final Random random = new Random();

    void generate(GenericScheduleModel<BasicResource, BasicTask> model, ZonedDateTime start, ZonedDateTime end) {
        List<BasicResource> resources = new ArrayList<>();
        for (int i = 100; i <= 200; ++i) resources.add(new BasicResource("Conference room " + i));
        resources.forEach(resource -> buildLineOfWork(resource, start, end, model));
    }

    int counter = 100;

    private void buildLineOfWork(BasicResource res, ZonedDateTime start, ZonedDateTime end, GenericScheduleModel<BasicResource, BasicTask> model) {
        ZonedDateTime currentTime = start;
        while (currentTime.isBefore(end)) {
            ZonedDateTime eventStart = fastForward(currentTime);
            currentTime = fastForward(eventStart);
            BasicTask event = new BasicTask(eventStart, currentTime, "Meeting # " + counter++);
            model.assign(res, event);
        }
    }

    private ZonedDateTime fastForward(ZonedDateTime currentTime) {
        currentTime = currentTime.plusHours(6 + random.nextInt(72)).plusMinutes(random.nextInt(60));
        return currentTime;
    }
}
