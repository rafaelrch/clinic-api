package com.clinicapi.domain.doctor;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    public List<Doctor> findAllByActiveTrue();

}
