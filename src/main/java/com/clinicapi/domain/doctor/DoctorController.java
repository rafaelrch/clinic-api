package com.clinicapi.domain.doctor;

import com.clinicapi.domain.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Doctors", description = "Doctor management")
public class DoctorController {

    @Autowired
    DoctorService service;

    @PostMapping
    @Operation(summary = "Register doctor", description = "Registers a new doctor and returns the created resource")
    @ApiResponse(responseCode = "201", description = "Doctor registered successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed! One or more fields are invalid")
    public ResponseEntity<DoctorResponseData> register(@RequestBody @Valid DoctorCreateData data, UriComponentsBuilder uriBuilder) {
        DoctorResponseData created = service.register(data);
        URI uri = uriBuilder.path("/doctors/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    @Operation(summary = "List all doctors", description = "Returns a paginated list of registered doctors active")
    @ApiResponse(responseCode = "200", description = "Doctors listed successfully")
    public Page<DoctorResponseData> list(@ParameterObject Pageable pageable){
        return service.list(pageable);
    }
    @GetMapping(value = "/{id}")
    @Operation(summary = "Find doctor by ID", description = "Returns the doctor with the given ID")
    @ApiResponse(responseCode = "200", description = "Doctor id found successfully")
    @ApiResponse(responseCode = "404", description = "Doctor id not found or inactive")
    public ResponseEntity<DoctorResponseData> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update doctor", description = "Updates the doctor data. Only provided fields are updated")
    @ApiResponse(responseCode = "200", description = "Doctor updated successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed! One or more fields are invalid")
    @ApiResponse(responseCode = "404", description = "Doctor id not found")
    public ResponseEntity<DoctorResponseData> update(@PathVariable Long id, @RequestBody @Valid DoctorUpdateData data){
        return ResponseEntity.ok().body(service.update(id, data));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete doctor", description = "Deactivates the doctor (soft delete). The record is preserved in the database")
    @ApiResponse(responseCode = "204", description = "Doctor deactivated successfully")
    @ApiResponse(responseCode = "404", description = "Doctor id not found")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
