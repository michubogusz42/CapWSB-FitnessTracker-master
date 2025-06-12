package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.training.api.ActivityType;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    // Find all trainings
    public List<Training> findAllTrainings() {
        return trainingRepository.findAll();
    }

    // Find trainings by userId
    public List<Training> findByUserId(Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    // Find finished trainings after a specific date
    public List<Training> findFinishedAfter(LocalDate afterDate) {
        Date cutoff = java.sql.Date.valueOf(afterDate);
        return trainingRepository.findAll().stream()
                .filter(t -> !t.getEndTime().before(cutoff))
                .collect(Collectors.toList());
    }

    // Find trainings by activity type
    public List<Training> findByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    // Create a new training
    public Training createTraining(TrainingCreateDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new TrainingNotFoundException(dto.userId()));
        Training t = new Training(
                user,
                java.sql.Timestamp.valueOf(dto.startTime()),
                java.sql.Timestamp.valueOf(dto.endTime()),
                dto.activityType(),
                dto.distance(),
                dto.averageSpeed());
        return trainingRepository.save(t);
    }

    // Update an existing training
    public Training updateTraining(Long id, TrainingCreateDto dto) {
        Training existing = trainingRepository.findById(id)
                .orElseThrow(() -> new TrainingNotFoundException(id));
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new TrainingNotFoundException(dto.userId()));
        existing.setUser(user);
        existing.setStartTime(java.sql.Timestamp.valueOf(dto.startTime()));
        existing.setEndTime(java.sql.Timestamp.valueOf(dto.endTime()));
        existing.setActivityType(dto.activityType());
        existing.setDistance(dto.distance());
        existing.setAverageSpeed(dto.averageSpeed());
        return trainingRepository.save(existing);
    }

    // Delete a training by ID
    public void deleteTraining(Long id) {
        Training existing = trainingRepository.findById(id)
                .orElseThrow(() -> new TrainingNotFoundException(id)); // Ensure training exists before deleting
        trainingRepository.delete(existing);  // Delete the training
    }

    // Find a training by ID (helper method for checking existence before deletion)
    public Optional<Training> findTrainingById(Long id) {
        return trainingRepository.findById(id);  // Return Optional, if present, the training exists
    }
}

