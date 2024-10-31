package com.velocity.app.mapper;

import com.velocity.app.dto.request.BirdRequestDto;
import com.velocity.app.dto.response.BirdResponseDto;
import com.velocity.app.model.Bird;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BirdMapper {
    @Mapping(target = "id", ignore = true)
    Bird birdRequestDtoToBird(BirdRequestDto birdRequestDto);

    BirdResponseDto birdToBirdResponseDto(Bird bird);
}
