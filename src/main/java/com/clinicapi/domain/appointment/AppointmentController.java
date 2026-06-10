package com.clinicapi.domain.appointment;


import com.clinicapi.domain.service.AppointmentService;

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
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "Appointment management")
public class AppointmentController {

    @Autowired
    AppointmentService service;

    @PostMapping
    @Operation(summary = "Register appointment", description = "Registers a new appointment and returns the created resource")
    @ApiResponse(responseCode = "201", description = "Appointment registered successfully")
    @ApiResponse(responseCode = "400", description = "Business rule violation: doctor inactive, scheduling conflict, or invalid date")
    public ResponseEntity<AppointmentResponseData> register(@RequestBody @Valid AppointmentCreateData data, UriComponentsBuilder uriBuilder) {
        AppointmentResponseData created = service.register(data);
        URI uri = uriBuilder.path("/appointments/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    @Operation(summary = "List all appointments", description = "Returns a paginated list of registered appointments")
    @ApiResponse(responseCode = "200", description = "Appointments listed successfully")
    public Page<AppointmentResponseData> list(@ParameterObject Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find appointment by ID", description = "Returns the appointment with the given ID")
    @ApiResponse(responseCode = "200", description = "Appointment id found successfully")
    @ApiResponse(responseCode = "404", description = "Appointment id not found")
    public ResponseEntity<AppointmentResponseData> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update appointment", description = "Updates the appointment date")
    @ApiResponse(responseCode = "200", description = "Appointment updated successfully")
    @ApiResponse(responseCode = "400", description = "Scheduling conflict: doctor or patient already has an appointment at this time")
    @ApiResponse(responseCode = "404", description = "Appointment id not found")
    public ResponseEntity<AppointmentResponseData> update(@PathVariable Long id, @RequestBody @Valid AppointmentUpdateData data){
        return ResponseEntity.ok().body(service.update(id, data));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment", description = "Changes the appointment status to CANCELLED and returns the updated resource")
    @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully, returns the updated appointment with CANCELLED status")
    @ApiResponse(responseCode = "400", description = "Just SCHEDULED or CONFIRMED status can be changed")
    @ApiResponse(responseCode = "404", description = "Appointment id not found")
    public ResponseEntity<AppointmentResponseData> cancel(@PathVariable Long id){
        return ResponseEntity.ok().body(service.cancel(id));
    }

}
