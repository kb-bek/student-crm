package com.kaitech.student_crm.payload.request;

import jakarta.validation.constraints.NotNull;

public record LevelRequest(@NotNull String title,
                           @NotNull String description,
                           @NotNull Integer pointFrom,
                           @NotNull Integer pointTo
) {
}