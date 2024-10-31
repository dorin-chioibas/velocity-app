package com.velocity.app.controller;

import com.velocity.app.dto.request.BirdRequestDto;
import com.velocity.app.dto.response.BirdResponseDto;
import com.velocity.app.service.BirdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/birds")
public class BirdController {
    private final BirdService birdService;

    @GetMapping
    public ResponseEntity<List<BirdResponseDto>> getAllBirds() {
        return ResponseEntity.ok(birdService.getAllBirds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BirdResponseDto> getBirdById(@PathVariable Long id) {
        return ResponseEntity.ok(birdService.findBirdById(id));
    }

    @PostMapping
    public ResponseEntity<BirdResponseDto> createBird(@RequestBody BirdRequestDto bird) {
        return ResponseEntity.status(HttpStatus.CREATED).body(birdService.saveBird(bird));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BirdResponseDto> updateBird(@PathVariable Long id, @RequestBody BirdRequestDto newBird) {
        return ResponseEntity.ok(birdService.updateBird(id, newBird));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBird(@PathVariable Long id) {
        birdService.deleteBird(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BirdResponseDto>> searchBirds(@RequestParam(required = false) String name, @RequestParam(required = false) String color) {
        return ResponseEntity.ok(birdService.findBirdsByNameAndColor(name, color));
    }
}
