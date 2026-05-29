package com.catchcbnu.ospp_project.submission.dto;

import java.time.LocalDateTime;

public record SubmissionAvailabilityResponse(
        Long sensorId,
        boolean available,
        LocalDateTime currentTimeSlot,
        boolean alreadySubmitted,
        LocalDateTime nextAvailableAt
) {
}
