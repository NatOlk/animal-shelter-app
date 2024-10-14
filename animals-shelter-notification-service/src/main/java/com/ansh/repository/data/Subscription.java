package com.ansh.repository.data;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subscribers", schema = "public")
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String email;
    @Column
    private String topic;

    private boolean accepted;

    public Subscription() {
    }
}
