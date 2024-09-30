package ru.golovkov.taskstn.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_hr")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String password;

    private String email;

    private String fio;

    private String photo;

    @OneToMany(mappedBy = "applicant", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Meeting> applicantMeetings;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Meeting> authorMeetings;

    @ManyToMany(mappedBy = "recipients", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Meeting> recipientMeetings;
}
