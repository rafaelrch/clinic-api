package com.clinicapi.domain.patient;

import com.clinicapi.domain.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Patient management")
public class PatientController {

    @Autowired
    PatientService service;

    @PostMapping
    @Operation(summary = "Register patient", description = "Registers a new patient and returns the created resource")
    @ApiResponse(responseCode = "201", description = "Patients registered successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed! One or more fields are invalid")
    public ResponseEntity<PatientResponseData> register(@RequestBody @Valid PatientCreateData data, UriComponentsBuilder uriBuilder) {
        PatientResponseData created = service.register(data);
        URI uri = uriBuilder.path("/patients/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    @Operation(summary = "List all patients", description = "Returns a paginated list of registered patients")
    @ApiResponse(responseCode = "200", description = "Patients listed successfully")
    public Page<PatientResponseData> list(@ParameterObject Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Find patient by ID", description = "Returns the patient with the given ID")
    @ApiResponse(responseCode = "200", description = "Patient id listed successfully")
    @ApiResponse(responseCode = "404", description = "Patient id not found")
    public ResponseEntity<PatientResponseData> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update patient", description = "Updates the patient data. Only provided fields are updated")
    @ApiResponse(responseCode = "200", description = "Patient updated successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed! One or more fields are invalid")
    @ApiResponse(responseCode = "404", description = "Patient id not found")
    public ResponseEntity<PatientResponseData> update(@PathVariable Long id, @RequestBody @Valid PatientUpdateData data){
        return ResponseEntity.ok().body(service.update(id, data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Permanently deletes the patient from the database")
    @ApiResponse(responseCode = "204", description = "Patient deleted successfully")
    @ApiResponse(responseCode = "404", description = "Patient id not found")
    @ApiResponse(responseCode = "409", description = "Patient has linked appointments and cannot be deleted")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
