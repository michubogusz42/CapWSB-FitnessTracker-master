package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.fitnesstracker.training.api.ActivityType;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.user.internal.UserRepository;  // <–– tutaj

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;  // <–– tutaj

    @GetMapping
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @GetMapping("/{userId}")
    public List<Training> getTrainingsForUser(@PathVariable Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    @GetMapping("/finished/{afterTime}")
    public List<Training> getFinishedAfter(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterTime) {
        Date cutoff = java.sql.Date.valueOf(afterTime);
        return trainingRepository.findAll().stream()
                .filter(t -> !t.getEndTime().before(cutoff))
                .collect(Collectors.toList());
    }

    @GetMapping("/activityType")
    public List<Training> getByActivityType(@RequestParam ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    @PostMapping
    public ResponseEntity<Training> createTraining(@RequestBody TrainingCreateDto dto) {
        var user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var training = new Training(
                user,
                java.sql.Timestamp.valueOf(dto.startTime()),
                java.sql.Timestamp.valueOf(dto.endTime()),
                dto.activityType(),
                dto.distance(),
                dto.averageSpeed()
        );
        var saved = trainingRepository.save(training);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<Training> updateTraining(
            @PathVariable Long trainingId,
            @RequestBody TrainingCreateDto dto) {
        var existing = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training not found"));
        var user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        existing.setUser(user);
        existing.setStartTime(java.sql.Timestamp.valueOf(dto.startTime()));
        existing.setEndTime(java.sql.Timestamp.valueOf(dto.endTime()));
        existing.setActivityType(dto.activityType());
        existing.setDistance(dto.distance());
        existing.setAverageSpeed(dto.averageSpeed());
        var updated = trainingRepository.save(existing);
        return ResponseEntity.ok(updated);
    }
}
