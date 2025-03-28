package com.example.schedule.dto.request;

import lombok.Getter;

@Getter
public class ScheduleUpdateRequestDto {
    private String content;
    private String password;
    private String writer;
}
