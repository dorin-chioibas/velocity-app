package com.velocity.app.service;

import com.velocity.app.dto.request.BirdRequestDto;
import com.velocity.app.dto.response.BirdResponseDto;
import com.velocity.app.exception.EntityNotFoundException;
import com.velocity.app.mapper.BirdMapper;
import com.velocity.app.repository.BirdRepository;
import com.velocity.app.repository.SightingRepository;
import com.velocity.app.repository.specification.BirdSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BirdService {
    private final BirdRepository birdRepository;
    private final SightingRepository sightingRepository;
    private final BirdSpecification birdSpecification;
    private final BirdMapper birdMapper;

    public List<BirdResponseDto> getAllBirds() {
        var allBirds = birdRepository.findAll();
        log.info("Found all birds");
        return allBirds.stream()
                .map(birdMapper::birdToBirdResponseDto)
                .toList();
    }

    public List<BirdResponseDto> findBirdsByNameAndColor(String name, String color) {
        var queriedBirds = birdRepository.findAll(birdSpecification.getByNameAndColor(name, color));
        log.info("Found birds by name {} and color {}", name, color);
        return queriedBirds.stream()
                .map(birdMapper::birdToBirdResponseDto)
                .toList();
    }

    public BirdResponseDto findBirdById(Long id) {
        var bird = birdRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Bird not found"));
        log.info("Found bird with id: {}", id);
        return birdMapper.birdToBirdResponseDto(bird);
    }

    public BirdResponseDto saveBird(BirdRequestDto birdRequestDto) {
        var savedBird = birdRepository.save(birdMapper.birdRequestDtoToBird(birdRequestDto));
        log.info("Saved bird with id: {}", savedBird.getId());
        return birdMapper.birdToBirdResponseDto(savedBird);
    }

    public BirdResponseDto updateBird(Long id, BirdRequestDto newBird) {
        var existingBird = birdRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Bird not found with id " + id));
        var updatedBird = birdMapper.birdRequestDtoToBird(newBird)
                .toBuilder()
                .id(existingBird.getId())
                .build();

        var savedUpdatedBird = birdRepository.save(updatedBird);
        log.info("Updated bird with id: {}", savedUpdatedBird.getId());
        return birdMapper.birdToBirdResponseDto(savedUpdatedBird);
    }

    public void deleteBird(Long id) {
        var sightingsForRemoval = sightingRepository.findByBirdId(id);
        sightingRepository.deleteAll(sightingsForRemoval);
        log.info("Deleted all sightings associated with bird id {}", id);
        birdRepository.deleteById(id);
        log.info("Deleted bird with id {}", id);
    }
}
