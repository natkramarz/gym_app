package uj.jwzp.kpnk.GymApp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uj.jwzp.kpnk.GymApp.service.EventService;


@Component
public class ScheduleEventsJob {

    private final EventService eventService;


    @Autowired
    public ScheduleEventsJob(EventService eventService) {
        this.eventService = eventService;
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void archiveEvents() {
        eventService.archiveEvents();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void addEvents() {
        eventService.addEvents();
    }

}
