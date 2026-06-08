package com.clinicapi.domain.doctor;

import com.clinicapi.domain.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public Page<DoctorResponseData> list(Pageable pageable){
        return service.list(pageable);
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
