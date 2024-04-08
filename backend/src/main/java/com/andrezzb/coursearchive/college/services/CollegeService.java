package com.andrezzb.coursearchive.college.services;

import com.andrezzb.coursearchive.college.dto.CollegeCreateDto;
import com.andrezzb.coursearchive.college.dto.CollegeUpdateDto;
import com.andrezzb.coursearchive.college.exceptions.CollegeNotFoundException;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import com.andrezzb.coursearchive.security.services.AclUtilService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
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
    public Page<College> findAllCollegesPaged(Pageable p) {
        return collegeRepository.findAll(p);
    }

    @PreAuthorize("hasRole('USER')")
    public College findCollegeById(Long id) {
        return collegeRepository.findById(id).orElseThrow(() -> new CollegeNotFoundException(id));
    }

    @Transactional
    @PreAuthorize("hasRole('MANAGER')")
    public College createCollege(CollegeCreateDto collegeDto) {
        College newCollege = modelMapper.map(collegeDto, College.class);
        College savedCollege = collegeRepository.save(newCollege);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        aclUtilService.grantPermission(savedCollege, username, BasePermission.ADMINISTRATION);
        return savedCollege;
    }

    @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.college.models.College', write) || hasRole('MANAGER')")
    public College updateCollege(Long id, CollegeUpdateDto collegeDto) {
        College college = findCollegeById(id);
        modelMapper.map(collegeDto, college);
        return collegeRepository.save(college);
    }

    @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.college.models.College', delete) || hasRole('MANAGER')")
    public void deleteCollegeById(Long id) {
        collegeRepository.deleteById(id);
    }
}
