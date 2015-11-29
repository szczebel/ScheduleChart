package schedule;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private final Random random = new Random();

    void generate(BasicScheduleModel<BasicResource, BasicEvent> model, ZonedDateTime start, ZonedDateTime end) {
        List<BasicResource> resources = new ArrayList<>();
        for (int i = 100; i <= 200; ++i) resources.add(new BasicResource("Registration : KR ABC" + i));
        resources.forEach(resource -> buildLineOfWork(resource, start, end, model));
    }

    private void buildLineOfWork(BasicResource res, ZonedDateTime start, ZonedDateTime end, BasicScheduleModel<BasicResource, BasicEvent> model) {
        ZonedDateTime currentTime = start;
        while (currentTime.isBefore(end)) {
            ZonedDateTime eventStart = fastForward(currentTime);
            currentTime = fastForward(eventStart);
            BasicEvent event = new BasicEvent(eventStart, currentTime, "John Doe");
            model.assign(res, event);
        }
    }

    private ZonedDateTime fastForward(ZonedDateTime currentTime) {
        currentTime = currentTime.plusHours(12 + random.nextInt(144)).plusMinutes(random.nextInt(60));
        return currentTime;
    }
}
