package pl.wsb.fitnesstracker.training.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.training.internal.TrainingRepository;
import pl.wsb.fitnesstracker.user.internal.UserRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingReportService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    public TrainingReportDto generateMonthlyReport(Long userId, int month, int year) {
        // Logika do pobierania i przetwarzania danych treningów
        List<Training> trainings = trainingRepository.findByUserIdAndDate(userId, month, year);
        // Przetwórz dane do raportu
        TrainingReportDto report = new TrainingReportDto();
        report.setTotalTrainings(trainings.size());
        report.setTotalDistance(trainings.stream().mapToDouble(Training::getDistance).sum());
        report.setAverageSpeed(trainings.stream().mapToDouble(Training::getAverageSpeed).average().orElse(0));
        return report;
    }
}
