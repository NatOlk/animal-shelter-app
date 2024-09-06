package com.example.gr.jpa.data;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subscribers", schema = "public")
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String email;

    @Column
    private String topic;

    private boolean accepted;

    public Subscription() {
    }
}
