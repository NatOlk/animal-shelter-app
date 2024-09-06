package com.example.gr.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gr.jpa.data.Animal;
import com.example.gr.jpa.data.AnimalKey;

public interface AnimalRepository extends JpaRepository<Animal, AnimalKey>
{
}
