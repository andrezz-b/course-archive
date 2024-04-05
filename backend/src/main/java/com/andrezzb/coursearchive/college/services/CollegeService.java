package com.andrezzb.coursearchive.college.services;

import com.andrezzb.coursearchive.college.dto.CollegeCreateDto;
import com.andrezzb.coursearchive.college.dto.CollegeUpdateDto;
import com.andrezzb.coursearchive.college.exceptions.CollegeNotFoundException;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final ModelMapper modelMapper;
    private final MutableAclService aclService;

    public CollegeService(CollegeRepository collegeRepository, ModelMapper modelMapper, MutableAclService aclService) {
        this.collegeRepository = collegeRepository;
        this.modelMapper = modelMapper;
        this.aclService = aclService;
    }

    @PreAuthorize("hasRole('USER')")
    public Page<College> findAllCollegesPaged(Pageable p) {
        return collegeRepository.findAll(p);
    }

    @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.college.models.College', admin)")
    public College findCollegeById(Long id) {
        return collegeRepository.findById(id).orElseThrow(() -> new CollegeNotFoundException(id));
    }

    @Transactional
    public College createCollege(CollegeCreateDto collegeDto) {
        College newCollege = modelMapper.map(collegeDto, College.class);
        var savedCollege = collegeRepository.save(newCollege);
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        ObjectIdentity oi = new ObjectIdentityImpl(newCollege);
        Sid sid = new PrincipalSid(username);
        Permission p = BasePermission.ADMINISTRATION;
        MutableAcl acl = null;
        try {
            acl = (MutableAcl) aclService.readAclById(oi);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oi);
        }

        // Now grant some permissions via an access control entry (ACE)
        acl.insertAce(acl.getEntries().size(), p, sid, true);
        aclService.updateAcl(acl);

        return savedCollege;
    }

    public College updateCollege(Long id, CollegeUpdateDto collegeDto) {
        College college = findCollegeById(id);
        modelMapper.map(collegeDto, college);
        return collegeRepository.save(college);
    }

    public void deleteCollegeById(Long id) {
        collegeRepository.deleteById(id);
    }
}
