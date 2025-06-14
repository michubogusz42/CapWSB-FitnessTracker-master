package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.fitnesstracker.training.api.ActivityType;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.user.internal.UserRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final UserRepository userRepository;

    // Get all trainings
    @GetMapping
    public List<Training> getAllTrainings() {
        return trainingService.findAllTrainings();
    }

    // Get trainings by userId
    @GetMapping("/{userId}")
    public List<Training> getTrainingsForUser(@PathVariable Long userId) {
        List<Training> trainings = trainingService.findByUserId(userId);
        if (trainings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No trainings found for user with ID: " + userId);
        }
        return trainings;
    }


    @GetMapping("/finished/{afterTime}")
    public List<Training> getFinishedAfter(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterTime) {
        return trainingService.findFinishedAfter(afterTime);
    }

    @GetMapping("/activityType")
    public List<Training> getByActivityType(@RequestParam ActivityType activityType) {
        return trainingService.findByActivityType(activityType);
    }

    @PostMapping
    public ResponseEntity<Training> createTraining(@RequestBody TrainingCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.createTraining(dto));
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<Training> updateTraining(
            @PathVariable Long trainingId,
            @RequestBody TrainingCreateDto dto) {
        return ResponseEntity.ok(trainingService.updateTraining(trainingId, dto));
    }

    @DeleteMapping("/{trainingId}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long trainingId) {
        // Upewnijmy się, że trening istnieje przed próbą usunięcia
        Training existing = trainingService.findTrainingById(trainingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training not found"));

        trainingService.deleteTraining(trainingId);
        return ResponseEntity.noContent().build();
    }

}
