package com.velocity.app.service;

import com.velocity.app.dto.request.SightingRequestDto;
import com.velocity.app.dto.response.SightingResponseDto;
import com.velocity.app.exception.EntityNotFoundException;
import com.velocity.app.mapper.SightingMapper;
import com.velocity.app.model.Sighting;
import com.velocity.app.repository.BirdRepository;
import com.velocity.app.repository.SightingRepository;
import com.velocity.app.repository.specification.SightingSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SightingService {
    private final SightingRepository sightingRepository;
    private final BirdRepository birdRepository;
    private final SightingSpecification sightingSpecification;
    private final SightingMapper sightingMapper;

    public List<SightingResponseDto> getAllSightings() {
        var allSightings = sightingRepository.findAll();
        log.info("Found all sightings");
        return allSightings.stream()
                .map(sightingMapper::sightingToSightingResponseDto)
                .toList();
    }

    public List<Sighting> findSightings(Long birdId, String location, LocalDateTime start, LocalDateTime end) {
        var sightings = sightingRepository.findAll(sightingSpecification.getByBirdLocationAndTime(birdId, location, start, end));
        log.info("Found sightings by id {} and location {} between {} and {}", birdId, location, start, end);
        return sightings;
    }

    public SightingResponseDto findSightingById(Long id) {
        var sighting = sightingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sighting not found"));
        log.info("Found sighting with id {}", id);
        return sightingMapper.sightingToSightingResponseDto(sighting);
    }

    public SightingResponseDto saveSighting(SightingRequestDto sightingRequestDto) {
        var existingBird = birdRepository.findById(sightingRequestDto.getBirdId()).orElseThrow(() -> new EntityNotFoundException("Bird not found"));
        var sightingToSave = sightingMapper.sightingRequestDtoToSighting(sightingRequestDto).toBuilder()
                .bird(existingBird)
                .build();
        var savedSighting = sightingRepository.save(sightingToSave);
        log.info("Saved sighting with id {}", savedSighting.getId());
        return sightingMapper.sightingToSightingResponseDto(savedSighting);
    }

    public SightingResponseDto updateSighting(Long id, SightingRequestDto sightingRequestDto) {
        var existingSighting = sightingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sighting not found"));
        var existingBird = birdRepository.findById(sightingRequestDto.getBirdId()).orElseThrow(() -> new EntityNotFoundException("Bird not found"));

        var sightingToUpdate = existingSighting.toBuilder()
                .bird(existingBird)
                .location(sightingRequestDto.getLocation())
                .dateTime(sightingRequestDto.getDateTime())
                .build();

        var updatedSighting = sightingRepository.save(sightingToUpdate);
        log.info("Updated sighting with id {}", updatedSighting.getId());
        return sightingMapper.sightingToSightingResponseDto(updatedSighting);
    }

    public void deleteSighting(Long id) {
        sightingRepository.deleteById(id);
        log.info("Deleted sighting with id {}", id);
    }
}
