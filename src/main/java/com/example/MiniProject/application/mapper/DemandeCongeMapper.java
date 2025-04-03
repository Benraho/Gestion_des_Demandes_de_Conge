package com.example.MiniProject.application.mapper;

import com.example.MiniProject.application.dto.DemandeCongeDTO;
import com.example.MiniProject.domain.model.DemandeConge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DemandeCongeMapper {
   // DemandeCongeMapper INSTANCE = Mappers.getMapper(DemandeCongeMapper.class);

    DemandeCongeDTO toDTO(DemandeConge entity);
    DemandeConge toEntity(DemandeCongeDTO dto);
}