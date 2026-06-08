package com.clinicapi.domain.appointment;


import com.clinicapi.domain.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService service;

    @PostMapping
    public void register(@RequestBody @Valid AppointmentCreateData data) {
        service.register(data);
    }

    @GetMapping
    public Page<AppointmentResponseData> list(Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public AppointmentResponseData findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid AppointmentUpdateData data){
        service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

}
