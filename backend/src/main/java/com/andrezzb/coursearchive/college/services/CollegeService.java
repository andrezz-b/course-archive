package com.andrezzb.coursearchive.college.services;

import com.andrezzb.coursearchive.college.dto.CollegeCreateDto;
import com.andrezzb.coursearchive.college.dto.CollegeUpdateDto;
import com.andrezzb.coursearchive.college.exceptions.CollegeNotFoundException;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final ModelMapper modelMapper;

    public CollegeService(CollegeRepository collegeRepository, ModelMapper modelMapper) {
        this.collegeRepository = collegeRepository;
        this.modelMapper = modelMapper;
    }

    public Page<College> findAllCollegesPaged(Pageable p) {
        return collegeRepository.findAll(p);
    }

    public College findCollegeById(Long id) {
        return collegeRepository.findById(id).orElseThrow(() -> new CollegeNotFoundException(id));
    }

    public College createCollege(CollegeCreateDto collegeDto) {
        College newCollege = modelMapper.map(collegeDto, College.class);
        return collegeRepository.save(newCollege);
    }

    public College updateCollege(Long id, CollegeUpdateDto collegeDto) {
        College college = findCollegeById(id);
        modelMapper.map(collegeDto, college);
        return collegeRepository.save(college);
    }
}
