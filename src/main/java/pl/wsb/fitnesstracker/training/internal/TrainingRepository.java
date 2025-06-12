package pl.wsb.fitnesstracker.training.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.ActivityType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Date;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByUserId(Long userId);
    List<Training> findByActivityType(ActivityType activityType);
    List<Training> findByEndTimeBefore(Date date);
    @Query("SELECT t FROM Training t WHERE t.user.id = :userId AND MONTH(t.startTime) = :month AND YEAR(t.startTime) = :year")
    List<Training> findByUserIdAndDate(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);
}

