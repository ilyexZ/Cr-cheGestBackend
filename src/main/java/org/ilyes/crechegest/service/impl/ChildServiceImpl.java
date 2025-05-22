package org.ilyes.crechegest.service.impl;

import org.ilyes.crechegest.model.Child;
import org.ilyes.crechegest.repository.ChildRepository;
import org.ilyes.crechegest.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;

    @Autowired
    public ChildServiceImpl(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Override
    public Child save(Child child) {
        return childRepository.save(child);
    }

    @Override
    public Optional<Child> findById(Long id) {
        return childRepository.findById(id);
    }

    @Override
    public List<Child> findAll() {
        return childRepository.findAll();
    }

    @Override
    public List<Child> findByParentId(Long parentId) {
        return childRepository.findByParentId(parentId);
    }

    @Override
    public List<Child> findActiveChildren() {
        return childRepository.findByActiveTrue();
    }

    @Override
    public List<Child> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        return childRepository.findByBirthDateBetween(startDate, endDate);
    }

    @Override
    public Child updateChild(Child child) {
        Optional<Child> existingChild = childRepository.findById(child.getId());
        if (existingChild.isPresent()) {
            Child updatedChild = existingChild.get();
            updatedChild.setFirstName(child.getFirstName());
            updatedChild.setLastName(child.getLastName());
            updatedChild.setBirthDate(child.getBirthDate());
            updatedChild.setGender(child.getGender());
//            updatedChild.setEntryDate(child.getEntryDate());
//            updatedChild.setExitDate(child.getExitDate());
//            updatedChild.setActive(child.isActive());
//            updatedChild.setParent(child.getParent());
            updatedChild.setMedicalInfo(child.getMedicalInfo());

            return childRepository.save(updatedChild);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        childRepository.deleteById(id);
    }

    @Override
    public void deactivateChild(Long id) {
        Optional<Child> child = childRepository.findById(id);
        child.ifPresent(c -> {
//            c.setActive(false);
            childRepository.save(c);
        });
    }

    @Override
    public void activateChild(Long id) {
        Optional<Child> child = childRepository.findById(id);
        child.ifPresent(c -> {
//            c.setActive(true);
            childRepository.save(c);
        });
    }
}