package com.feeras.morgensonne.cron;

import com.feeras.morgensonne.service.ContentGrabber;
import com.feeras.morgensonne.service.WordpressClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    private final WordpressClient wordpressClient;
    private final ContentGrabber contentGrabber;

    public ScheduledTasks(WordpressClient wordpressClient, ContentGrabber contentGrabber) {
        this.wordpressClient = wordpressClient;
        this.contentGrabber = contentGrabber;
        try {
            schedulePostCreation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 0/15 8-19 * * MON-FRI")
    public void schedulePostCreation() throws Exception {
        List<String> images = contentGrabber.getImages();
        wordpressClient.createPost(images);
    }
}
