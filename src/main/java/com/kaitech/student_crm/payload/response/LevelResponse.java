package com.kaitech.student_crm.payload.response;

public record LevelResponse(Long id,
                            String title,
                            String description,
                            Integer pointFrom,
                            Integer pointTo) {
}