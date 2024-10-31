package com.velocity.app.mapper;

import com.velocity.app.dto.request.SightingRequestDto;
import com.velocity.app.dto.response.SightingResponseDto;
import com.velocity.app.model.Sighting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SightingMapper {
    @Mapping(target = "bird.id", source = "birdId")
    @Mapping(target = "id", ignore = true)
    Sighting sightingRequestDtoToSighting(SightingRequestDto sightingRequestDto);

    @Mapping(source = "bird.id", target = "bird.id")
    SightingResponseDto sightingToSightingResponseDto(Sighting sighting);
}
