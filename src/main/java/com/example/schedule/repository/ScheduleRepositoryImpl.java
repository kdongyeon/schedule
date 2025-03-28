package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
    public class ScheduleRepositoryImpl implements ScheduleRepository {

        private final JdbcTemplate jdbcTemplate;

        public ScheduleRepositoryImpl(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        @Override
        public Schedule save(Schedule schedule) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            LocalDateTime now = LocalDateTime.now();
            schedule.setCreateAt(now);
            schedule.setUpdateAt(now);

            String sql = "INSERT INTO schedule (content, password, writer, create_at, update_at) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, schedule.getContent());
                ps.setString(2, schedule.getPassword());
                ps.setString(3, schedule.getWriter());
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(schedule.getCreateAt()));
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(schedule.getUpdateAt()));
                return ps;
            }, keyHolder);

            schedule.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            return schedule;
        }

        @Override
        public List<Schedule> findAll(String updatedDate, String memberName) {
            StringBuilder sql = new StringBuilder("SELECT id, content, writer, create_at, update_at FROM schedule WHERE 1=1 ");
            List<Object> params = new ArrayList<>();

            if (updatedDate != null && !updatedDate.isEmpty()) {
                sql.append("AND DATE(update_at) = ? ");
                params.add(updatedDate);
            }
            if (memberName != null && !memberName.isEmpty()) {
                sql.append("AND writer = ? ");
                params.add(memberName);
            }
            sql.append("ORDER BY update_at DESC");

            return jdbcTemplate.query(
                    sql.toString(),
                    (rs, rowNum) -> new Schedule(
                            rs.getLong("id"),
                            rs.getString("content"),
                            rs.getString("writer"),
                            rs.getTimestamp("create_at").toLocalDateTime(),
                            rs.getTimestamp("update_at").toLocalDateTime()
                    ),
                    params.toArray(new Object[0])
            );
        }

        @Override
        public Optional<Schedule> findById(Long id) {
            String sql = "SELECT id, content, password, writer, create_at, update_at FROM schedule WHERE id = ?";
            List<Schedule> list = jdbcTemplate.query(sql, (rs, rowNum) -> new Schedule(
                    rs.getLong("id"),
                    rs.getString("content"),
                    rs.getString("password"),
                    rs.getString("writer"),
                    rs.getTimestamp("create_at").toLocalDateTime(),
                    rs.getTimestamp("update_at").toLocalDateTime()
            ), id);
            return list.stream().findAny();
        }

        @Override
        public Schedule update(Schedule schedule) {

            LocalDateTime now = LocalDateTime.now();
            schedule.setUpdateAt(now);

            String sql = "UPDATE schedule SET content = ?, writer = ?, update_at=? WHERE id = ?";
            jdbcTemplate.update(sql, schedule.getContent(), schedule.getWriter(), schedule.getUpdateAt(), schedule.getId());
            return findById(schedule.getId()).orElseThrow(() -> new IllegalStateException("일정 수정 후 조회 실패"));
        }

        @Override
        public void deleteById(Long id) {
            String sql = "DELETE FROM schedule WHERE id = ?";
            jdbcTemplate.update(sql, id);
        }
}
