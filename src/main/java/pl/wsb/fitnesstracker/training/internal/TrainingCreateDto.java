package pl.wsb.fitnesstracker.training.internal;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.wsb.fitnesstracker.training.api.ActivityType;

import java.time.LocalDateTime;

public record TrainingCreateDto(
        Long userId,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startTime,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endTime,

        ActivityType activityType,

        double distance,
        double averageSpeed
) {}
