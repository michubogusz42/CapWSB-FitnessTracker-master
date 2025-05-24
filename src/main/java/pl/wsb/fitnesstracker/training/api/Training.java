package pl.wsb.fitnesstracker.training.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.wsb.fitnesstracker.user.api.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Table(name = "trainings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_time", nullable = false)
    @JsonIgnore
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    @JsonIgnore
    private Date endTime;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(name = "distance")
    private double distance;

    @Column(name = "average_speed")
    private double averageSpeed;

    public Training(
            User user,
            Date startTime,
            Date endTime,
            ActivityType activityType,
            double distance,
            double averageSpeed
    ) {
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityType = activityType;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
    }

    public void setUser(User user) { this.user = user; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public void setActivityType(ActivityType activityType) { this.activityType = activityType; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setAverageSpeed(double averageSpeed) { this.averageSpeed = averageSpeed; }

    @JsonProperty("startTime")
    public String getStartTimeIso() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String iso = sdf.format(this.startTime);
        if (iso.endsWith("Z")) {
            iso = iso.substring(0, iso.length() - 1) + "+00:00";
        }
        return iso;
    }

    @JsonProperty("endTime")
    public String getEndTimeIso() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String iso = sdf.format(this.endTime);
        if (iso.endsWith("Z")) {
            iso = iso.substring(0, iso.length() - 1) + "+00:00";
        }
        return iso;
    }
}

