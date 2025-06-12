package pl.wsb.fitnesstracker.mail.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.training.api.TrainingReportDto;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReportEmail(String toEmail, TrainingReportDto report) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Monthly Training Report");
        message.setText("Here is your monthly training report:\n" +
                "Total Trainings: " + report.getTotalTrainings() + "\n" +
                "Total Distance: " + report.getTotalDistance() + "\n" +
                "Average Speed: " + report.getAverageSpeed());
        mailSender.send(message);
    }
}
