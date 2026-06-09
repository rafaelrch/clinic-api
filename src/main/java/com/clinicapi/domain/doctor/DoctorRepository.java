package com.clinicapi.domain.doctor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    public Page<Doctor> findAllByActiveTrue(Pageable pageable);

    public Optional<Doctor> findByIdAndActiveTrue(Long id);

}
