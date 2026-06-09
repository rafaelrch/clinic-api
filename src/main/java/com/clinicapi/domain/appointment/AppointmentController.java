package com.clinicapi.domain.appointment;


import com.clinicapi.domain.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService service;

    @PostMapping
    public ResponseEntity<AppointmentResponseData> register(@RequestBody @Valid AppointmentCreateData data, UriComponentsBuilder uriBuilder) {
        AppointmentResponseData created = service.register(data);
        URI uri = uriBuilder.path("/appointments/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public Page<AppointmentResponseData> list(Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseData> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseData> update(@PathVariable Long id, @RequestBody @Valid AppointmentUpdateData data){
        return ResponseEntity.ok().body(service.update(id, data));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseData> cancel(@PathVariable Long id){
        return ResponseEntity.ok().body(service.cancel(id));
    }

}
