package pl.wsb.fitnesstracker.training.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.wsb.fitnesstracker.mail.internal.EmailSender;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/v1/reports")
public class ReportingController {

    @Autowired
    private TrainingReportService trainingReportService;

    @Autowired
    private EmailSender emailSender;

    @GetMapping("/generate")
    public ResponseEntity<TrainingReportDto> generateMonthlyReport(
            @RequestParam Long userId,
            @RequestParam int month,
            @RequestParam int year) {
        TrainingReportDto report = trainingReportService.generateMonthlyReport(userId, month, year);


        emailSender.sendReportEmail("example@example.com", report);

        return ResponseEntity.ok(report);
    }
}
