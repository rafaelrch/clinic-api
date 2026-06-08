package com.clinicapi.domain.patient;

import com.clinicapi.domain.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    PatientService service;

    @PostMapping
    public void register(@RequestBody @Valid PatientCreateData data) {
        service.register(data);
    }

    @GetMapping
    public Page<PatientResponseData> list(Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping(value = "/{id}")
    public PatientResponseData findById(@PathVariable Long id){
        PatientResponseData obj = service.findById(id);
        return obj;
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid PatientUpdateData data){
        service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
