package com.clinicapi.domain.doctor;

import com.clinicapi.domain.patient.Patient;
import com.clinicapi.domain.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    DoctorService service;

    @PostMapping
    public void register(@RequestBody @Valid DoctorCreateData data) {
        service.register(data);
    }

    @GetMapping
    public List<DoctorResponseData> list(){
        return service.list();
    }
    @GetMapping(value = "/{id}")
    public DoctorResponseData findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable Long id, @RequestBody DoctorUpdateData data){
        service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
