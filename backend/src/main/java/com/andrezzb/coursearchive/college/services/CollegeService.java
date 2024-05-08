package com.andrezzb.coursearchive.college.services;

import com.andrezzb.coursearchive.college.dto.CollegeCreateDto;
import com.andrezzb.coursearchive.college.dto.CollegeDto;
import com.andrezzb.coursearchive.college.dto.CollegeUpdateDto;
import com.andrezzb.coursearchive.college.exceptions.CollegeNotFoundException;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final ModelMapper modelMapper;
    private final AclUtilService aclUtilService;

    public CollegeService(CollegeRepository collegeRepository, ModelMapper modelMapper,
            AclUtilService aclUtilService) {
        this.collegeRepository = collegeRepository;
        this.modelMapper = modelMapper;
        this.aclUtilService = aclUtilService;
    }

    @PreAuthorize("hasRole('USER')")
    public Page<CollegeDto> findAllCollegesPaged(Pageable p) {
        Page<College> colleges = collegeRepository.findAll(p);
        return colleges.map(college -> modelMapper.map(college, CollegeDto.class));
    }

    @PreAuthorize("hasRole('USER')")
    public CollegeDto findCollegeById(Long id) {
        College college = findCollege(id);
        return modelMapper.map(college, CollegeDto.class);
    }

    public College findCollege(Long id) {
        return collegeRepository.findById(id).orElseThrow(() -> new CollegeNotFoundException(id));
    }

    @Transactional
    @PreAuthorize("hasRole('MANAGER')")
    public CollegeDto createCollege(CollegeCreateDto createDto) {
        College newCollege = modelMapper.map(createDto, College.class);
        College savedCollege = collegeRepository.save(newCollege);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        aclUtilService.grantPermission(savedCollege, username, AclPermission.ADMINISTRATION);
        return modelMapper.map(savedCollege, CollegeDto.class);
    }

    @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.college.models.College', write) || hasRole('MANAGER')")
    public CollegeDto updateCollege(Long id, CollegeUpdateDto updateDto) {
        College college = findCollege(id);
        modelMapper.map(updateDto, college);
        College savedCollege = collegeRepository.save(college);
        return modelMapper.map(savedCollege, CollegeDto.class);
    }

    @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.college.models.College', delete) || hasRole('MANAGER')")
    public void deleteCollegeById(Long id) {
        collegeRepository.deleteById(id);
    }
}
