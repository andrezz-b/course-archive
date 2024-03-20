package com.andrezzb.coursearchive.college.services;

import com.andrezzb.coursearchive.college.exceptions.CollegeNotFoundException;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;

    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    public Page<College> findAllCollegesPaged(Pageable p) {
        return collegeRepository.findAll(p);
    }

    public College findCollegeById(Long id) {
        return collegeRepository.findById(id).orElseThrow(() -> new CollegeNotFoundException(id));
    }
}
