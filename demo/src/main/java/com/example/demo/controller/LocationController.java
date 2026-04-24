package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Location;
import com.example.demo.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationRepository repo;

    @GetMapping
    public List<Location> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Location getById(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Location not found");
        }

        repo.deleteById(id);
    }
}