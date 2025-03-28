package com.example.schedule.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    @Setter private Long id;
    private String content;
    private String password;
    private String writer;
    @Setter private LocalDateTime createAt;
    @Setter private LocalDateTime updateAt;


    public Schedule(String content, String password, String writer){

        this.content = content;
        this.password = password;
        this.writer = writer;

    }

    public Schedule(Long id, String content, String writer, LocalDateTime createAt, LocalDateTime updateAt){
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public void update(String content, String writer){
        this.content = content;
        this.writer = writer;

    }
}
