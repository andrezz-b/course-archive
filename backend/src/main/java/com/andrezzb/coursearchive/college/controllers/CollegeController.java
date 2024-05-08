package com.andrezzb.coursearchive.college.controllers;

import com.andrezzb.coursearchive.college.dto.CollegeCreateDto;
import com.andrezzb.coursearchive.college.dto.CollegeDto;
import com.andrezzb.coursearchive.college.dto.CollegeUpdateDto;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.services.CollegeService;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("api/college")
public class CollegeController {

    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<CollegeDto>> getAllColleges(
            @PositiveOrZero @RequestParam(defaultValue = "0") int page,
            @Positive @RequestParam(defaultValue = "5") int size,
            @ValidEnum(enumClazz = Sort.Direction.class,
                    ignoreCase = true) @RequestParam(defaultValue = "asc") String sortDirection,
            @ValidEnum(enumClazz = College.SortField.class) @RequestParam(
                    defaultValue = "id") String sortField,
            @ValidEnum(enumClazz = College.FilterField.class, required = false) @RequestParam(
                    required = false) String filterField,
            @RequestParam(required = false) String filterValue) {
        Object filterValueObj = College.FilterField.mapFilterValue(filterField, filterValue);
        Pageable p = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        final var collegesPaged =
                collegeService.findAllCollegesPaged(p, filterField, filterValueObj);
        return ResponseEntity.ok(collegesPaged);
    }

    @PostMapping("/")
    public ResponseEntity<CollegeDto> createCollege(@Valid @RequestBody CollegeCreateDto college) {
        final var createdCollege = collegeService.createCollege(college);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCollege);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CollegeDto> getCollegeById(@PathVariable Long id) {
        final var college = collegeService.findCollegeById(id);
        return ResponseEntity.ok(college);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollegeDto> updateCollegeById(@PathVariable Long id,
            @Valid @RequestBody CollegeUpdateDto collegeUpdateDto) {
        var updateCollege = collegeService.updateCollege(id, collegeUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCollege);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollegeById(@PathVariable Long id) {
        collegeService.deleteCollegeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
