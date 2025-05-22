package org.ilyes.crechegest.service.impl;

import org.ilyes.crechegest.model.Parent;
import org.ilyes.crechegest.repository.ParentRepository;
import org.ilyes.crechegest.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;

    @Autowired
    public ParentServiceImpl(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    @Override
    public Parent save(Parent parent) {
        return parentRepository.save(parent);
    }

    @Override
    public Optional<Parent> findById(Long id) {
        return parentRepository.findById(id);
    }

    @Override
    public List<Parent> findAll() {
        return parentRepository.findAll();
    }

    @Override
    public Optional<Parent> findByEmail(String email) {
        return parentRepository.findByEmail(email);
    }

    @Override
    public Optional<Parent> findByPhone(String phone) {
        return parentRepository.findByPhone(phone);
    }

    @Override
    public Parent updateParent(Parent parent) {
        Optional<Parent> existingParent = parentRepository.findById(parent.getId());
        if (existingParent.isPresent()) {
            Parent updatedParent = existingParent.get();
            updatedParent.setFirstName(parent.getFirstName());
            updatedParent.setLastName(parent.getLastName());
            updatedParent.setEmail(parent.getEmail());
            updatedParent.setPhone(parent.getPhone());
            updatedParent.setAddress(parent.getAddress());
//            updatedParent.setEmergencyContact(parent.getEmergencyContact());
//            updatedParent.setEmergencyPhone(parent.getEmergencyPhone());
//            updatedParent.setRelationship(parent.getRelationship());

            return parentRepository.save(updatedParent);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        parentRepository.deleteById(id);
    }
}