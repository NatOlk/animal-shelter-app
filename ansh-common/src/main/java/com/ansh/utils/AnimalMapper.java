package com.ansh.utils;

import com.ansh.dto.AnimalDTO;
import com.ansh.entity.animal.Animal;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnimalMapper {
  AnimalDTO toDto(Animal animal);
  Animal toEntity(AnimalDTO dto);
  List<AnimalDTO> toDto(List<Animal> animals);
}