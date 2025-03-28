package com.example.schedule.controller;

import com.example.schedule.dto.request.ScheduleSaveRequestDto;
import com.example.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.schedule.dto.response.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> save(@RequestBody ScheduleSaveRequestDto requestDto){
        return ResponseEntity.ok(scheduleService.save(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll(
            @RequestParam(required = false) String updateDate,
            @RequestParam(required = false) String writer){
        return ResponseEntity.ok(scheduleService.findAll(updateDate, writer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id){
        return ResponseEntity.ok(scheduleService.findScheduleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateRequestDto request
    ){
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id, @RequestParam String password){
        scheduleService.deleteSchedule(id, password);
    }

}
