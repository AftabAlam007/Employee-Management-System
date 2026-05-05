package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.EmployeeDto;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee createdEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/by-email")
    public ResponseEntity<Employee> getEmployeeByEmail(@RequestParam String email) {
        Employee employee = employeeService.getEmployeeByEmail(email);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        if (updatedEmployee != null) {
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/payslip")
    @PreAuthorize("hasRole('ADMIN') or @employeeServiceImpl.isEmployeeOwner(authentication.name, #id)")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        byte[] payslip = employeeService.downloadPayslip(id, username);
        if (payslip != null) {
            MediaType pdfMediaType = org.springframework.http.MediaType.APPLICATION_PDF;
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"payslip_" + id + ".pdf\"")
                    .contentType(pdfMediaType)
                    .body(payslip);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
