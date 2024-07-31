package com.kaitech.student_crm.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceItemRequest(@NotNull(message = "Title cannot be null")
                                 @NotBlank(message = "Title cannot be blank") String title,
                                 @NotNull(message = "Description cannot be null")
                                 @NotBlank(message = "Description cannot be blank") String description,
                                 @NotNull(message = "ServiceId cannot be null")
                                 @Min(value = 0, message = "The serviceId cannot be negative") Long serviceId) {
}