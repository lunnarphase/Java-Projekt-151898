package org.example.javaprojekt151898.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

    @Test
    void onCreate_shouldSetAppliedAtIfNull() {
        Application application = new Application();
        application.onCreate();
        assertThat(application.getAppliedAt()).isNotNull();
        assertThat(application.getAppliedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void onCreate_shouldNotOverrideAppliedAtIfAlreadySet() {
        Application application = new Application();
        LocalDateTime customDate = LocalDateTime.of(2020, 1, 1, 12, 0);
        application.setAppliedAt(customDate);
        application.onCreate();
        assertThat(application.getAppliedAt()).isEqualTo(customDate);
    }
} 