package com.example.schedule.service;

import com.example.schedule.dto.request.ScheduleSaveRequestDto;
import com.example.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.schedule.dto.response.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResponseDto save(ScheduleSaveRequestDto requestDto) {
        Schedule schedule = new Schedule(
                requestDto.getContent(),
                requestDto.getPassword(),
                requestDto.getWriter()
        );


        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(
                savedSchedule.getId(),
                savedSchedule.getContent(),
                savedSchedule.getWriter(),
                savedSchedule.getCreateAt(),
                savedSchedule.getUpdateAt()
        );
    }
    @SneakyThrows
    @Transactional(readOnly = true)
    public ScheduleResponseDto findScheduleById(Long id){
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalAccessException("해당 ID의 스케쥴이 존재하지 않습니다.")
        );

        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getContent(),
                schedule.getWriter(),
                schedule.getCreateAt(),
                schedule.getUpdateAt()
        );
    }
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> findAll(String updatedDate, String memberName) {
        List<Schedule> schedules = scheduleRepository.findAll(updatedDate, memberName);

        return schedules.stream().map(schedule -> new ScheduleResponseDto(
                schedule.getId(),
                schedule.getContent(),
                schedule.getWriter(),
                schedule.getCreateAt(),
                schedule.getUpdateAt()
        )).toList();
    }
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto request) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 스케줄이 존재하지 않습니다.")
        );
        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        schedule.update(request.getContent(), request.getWriter());
        Schedule updatedSchedule = scheduleRepository.update(schedule);

        return new ScheduleResponseDto(
                updatedSchedule.getId(),
                updatedSchedule.getContent(),
                updatedSchedule.getWriter(),
                updatedSchedule.getCreateAt(),
                updatedSchedule.getUpdateAt()
        );
    }

    @Transactional
    public void deleteSchedule(Long id, String password) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 스케줄이 존재하지 않습니다.")
        );

        if (!schedule.getPassword().equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        scheduleRepository.deleteById(id);

    }
}
