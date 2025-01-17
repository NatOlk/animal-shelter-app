package com.ansh.utils;

import com.ansh.dto.VaccinationDTO;
import com.ansh.entity.animal.Vaccination;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccinationMapper {

  VaccinationDTO toDto(Vaccination animal);

  Vaccination toEntity(VaccinationDTO dto);

  List<VaccinationDTO> toDto(List<Vaccination> animals);
}