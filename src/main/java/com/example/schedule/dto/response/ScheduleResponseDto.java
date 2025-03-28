package com.example.schedule.dto.response;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {

    private final Long id;
    private final String content;
    private final String writer;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;

    public ScheduleResponseDto(Long id, String content, String writer, LocalDateTime createAt, LocalDateTime updateAt ){
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }


}
