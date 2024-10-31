package com.velocity.app.controller;

import com.velocity.app.dto.request.SightingRequestDto;
import com.velocity.app.dto.response.SightingResponseDto;
import com.velocity.app.model.Sighting;
import com.velocity.app.service.SightingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sightings")
public class SightingController {
    private final SightingService sightingService;

    @GetMapping
    public ResponseEntity<List<SightingResponseDto>> getAllSightings() {
        return ResponseEntity.ok(sightingService.getAllSightings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SightingResponseDto> getSightingById(@PathVariable Long id) {
        return ResponseEntity.ok(sightingService.findSightingById(id));
    }

    @PostMapping
    public ResponseEntity<SightingResponseDto> createSighting(@RequestBody SightingRequestDto sightingRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sightingService.saveSighting(sightingRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SightingResponseDto> updateSighting(@PathVariable Long id, @RequestBody SightingRequestDto sightingRequestDto) {
        return ResponseEntity.ok(sightingService.updateSighting(id, sightingRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSighting(@PathVariable Long id) {
        sightingService.deleteSighting(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Sighting> searchSightings(@RequestParam(required = false) Long birdId,
                                          @RequestParam(required = false) String location,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return sightingService.findSightings(birdId, location, startTime, endTime);
    }
}
