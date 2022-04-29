package com.example.api.entities;

import com.example.api.entities.enums.Status;
import lombok.*;

import javax.persistence.*;

@Entity(name = "story")
@Table(name = "story")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "assignee", nullable = false)
    private String assignee;
}
