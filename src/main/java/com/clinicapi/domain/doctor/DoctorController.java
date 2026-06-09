package com.clinicapi.domain.doctor;

import com.clinicapi.domain.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    DoctorService service;

    @PostMapping
    public ResponseEntity<DoctorResponseData> register(@RequestBody @Valid DoctorCreateData data, UriComponentsBuilder uriBuilder) {
        DoctorResponseData created = service.register(data);
        URI uri = uriBuilder.path("/doctors/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public Page<DoctorResponseData> list(Pageable pageable){
        return service.list(pageable);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<DoctorResponseData> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DoctorResponseData> update(@PathVariable Long id, @RequestBody @Valid DoctorUpdateData data){
        return ResponseEntity.ok().body(service.update(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
