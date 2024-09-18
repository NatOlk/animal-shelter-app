package com.example.gr.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gr.jpa.data.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long>
{
}
