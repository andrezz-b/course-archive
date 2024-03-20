package com.andrezzb.coursearchive.college.controllers;

import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.services.CollegeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/college")
public class CollegeController {

    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<College>> getAllColleges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "id") String sortField
    ) {
        Sort.Direction direction = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable p = PageRequest.of(page, size, Sort.by(direction, sortField));
        final var collegesPaged = collegeService.findAllCollegesPaged(p);
        return ResponseEntity.ok(collegesPaged);
    }


    @GetMapping("/{id}")
    public ResponseEntity<College> getCollegeById(@PathVariable Long id) {
        final var college = collegeService.findCollegeById(id);
        return ResponseEntity.ok(college);
    }
}
