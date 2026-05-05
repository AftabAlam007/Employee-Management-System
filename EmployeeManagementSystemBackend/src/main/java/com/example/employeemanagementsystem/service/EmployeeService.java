package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.EmployeeDto;
import com.example.employeemanagementsystem.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee createEmployee(EmployeeDto employeeDto);
    Employee getEmployeeById(Long id);
    List<Employee> getAllEmployees();
    Employee updateEmployee(Long id, EmployeeDto employeeDto);
    void deleteEmployee(Long id);
    boolean isEmployeeOwner(String username, Long employeeId);
    Employee getEmployeeByEmail(String email);
    byte[] downloadPayslip(Long employeeId, String username);
    Optional<Employee> getEmployeeByName(String name);
    Employee getHighestSalaryEmployee();
}

