package com.edevare.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pomodoro_sessions")
public class PomodoroSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    private int durationMinutes;
    private String sessionType; // WORK / BREAK
    private LocalDateTime timestamp = LocalDateTime.now();

    // Getters y Setters...
    public Long getIdSession() { return idSession; }
    public void setIdSession(Long idSession) { this.idSession = idSession; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    public LocalDateTime getTimestamp() { return timestamp; }
}