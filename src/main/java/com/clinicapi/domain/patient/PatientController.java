package com.clinicapi.domain.patient;

import com.clinicapi.domain.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    PatientService service;

    @PostMapping
    public ResponseEntity<PatientResponseData> register(@RequestBody @Valid PatientCreateData data, UriComponentsBuilder uriBuilder) {
        PatientResponseData created = service.register(data);
        URI uri = uriBuilder.path("/patients/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public Page<PatientResponseData> list(Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PatientResponseData> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PatientResponseData> update(@PathVariable Long id, @RequestBody @Valid PatientUpdateData data){
        return ResponseEntity.ok().body(service.update(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
