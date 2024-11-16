package com.feeras.morgensonne;

import com.feeras.morgensonne.cron.ScheduledTasks;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MorgensonneApplicationTests {

    @MockBean
    ScheduledTasks tasks;

    static {
        System.setProperty("username", "foo");
        System.setProperty("password", "bar");
    }

    @Test
    void contextLoads() {
    }

}
