package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.AIActionRequest;
import com.example.employeemanagementsystem.dto.EmployeeDto;
import com.example.employeemanagementsystem.dto.UserDto;
import com.example.employeemanagementsystem.model.Address;
import com.example.employeemanagementsystem.model.BankDetails;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.model.Finance;
import com.example.employeemanagementsystem.model.PersonalDetails;
import com.example.employeemanagementsystem.model.ProfessionalDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AIActionService {

    private final ObjectMapper objectMapper;
    private final EmployeeService employeeService;

    public AIActionService(ObjectMapper objectMapper, EmployeeService employeeService) {
        this.objectMapper = objectMapper;
        this.employeeService = employeeService;
    }

    public Object executeAction(String aiJson) {
        AIActionRequest request = parse(aiJson);
        String action = normalizeAction(request.getAction());

        return switch (action) {
            case "GET_ALL" -> employeeService.getAllEmployees();
            case "ADD" -> addEmployee(request);
            case "UPDATE" -> updateSalary(request);
            case "DELETE" -> deleteEmployee(request);
            case "MAX_SALARY" -> employeeService.getHighestSalaryEmployee();
            case "FIND_BY_NAME" -> findByName(request);
            case "GET_JOINING_DATE" -> getJoiningDate(request);
            case "GET_SALARY" -> getSalary(request);
            case "MIN_SALARY" -> getMinSalary();
            case "AVG_SALARY" -> getAverageSalary();
            case "COUNT_EMPLOYEES" -> Map.of("count", employeeService.getAllEmployees().size());
            case "GET_BY_MANAGER" -> getByManager(request);
            case "GET_BY_PROJECT" -> getByProject(request);
            default -> throw new IllegalArgumentException("Unknown command action: " + request.getAction());
        };
    }

    private AIActionRequest parse(String aiJson) {
        try {
            return objectMapper.readValue(aiJson, AIActionRequest.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON from AI model", e);
        }
    }

    private String normalizeAction(String action) {
        return action == null ? "" : action.trim().toUpperCase(Locale.ROOT);
    }

    private Employee addEmployee(AIActionRequest request) {
        validateName(request);
        validateSalary(request);
        EmployeeDto dto = createDefaultEmployeeDto(request.getName(), request.getSalary());
        return employeeService.createEmployee(dto);
    }

    private Employee updateSalary(AIActionRequest request) {
        validateName(request);
        validateSalary(request);

        Employee employee = employeeService.getEmployeeByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getName()));

        EmployeeDto dto = mapEmployeeToDto(employee);
        dto.getFinance().setCtcBreakup(String.valueOf(request.getSalary()));
        return employeeService.updateEmployee(employee.getId(), dto);
    }

    private String deleteEmployee(AIActionRequest request) {
        validateName(request);
        Employee employee = employeeService.getEmployeeByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getName()));
        employeeService.deleteEmployee(employee.getId());
        return "Employee deleted: " + request.getName();
    }

    private Employee findByName(AIActionRequest request) {
        validateName(request);
        return employeeService.getEmployeeByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getName()));
    }

    private Map<String, Object> getJoiningDate(AIActionRequest request) {
        validateName(request);
        Employee employee = employeeService.getEmployeeByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getName()));
        LocalDate joiningDate = employee.getProfessionalDetails() != null
                ? employee.getProfessionalDetails().getDateOfJoining()
                : null;
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("name", employee.getPersonalDetails() != null ? employee.getPersonalDetails().getFullName() : request.getName());
        response.put("dateOfJoining", joiningDate);
        return response;
    }

    private Map<String, Object> getSalary(AIActionRequest request) {
        validateName(request);
        Employee employee = employeeService.getEmployeeByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getName()));
        return Map.of(
                "name", employee.getPersonalDetails() != null ? employee.getPersonalDetails().getFullName() : request.getName(),
                "salary", extractSalary(employee)
        );
    }

    private Employee getMinSalary() {
        return employeeService.getAllEmployees()
                .stream()
                .min(Comparator.comparingInt(this::extractSalary))
                .orElse(null);
    }

    private Map<String, Object> getAverageSalary() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) {
            return Map.of("averageSalary", 0);
        }
        double average = employees.stream().mapToInt(this::extractSalary).average().orElse(0);
        return Map.of("averageSalary", Math.round(average * 100.0) / 100.0);
    }

    private List<Employee> getByManager(AIActionRequest request) {
        validateManagerName(request);
        return employeeService.getAllEmployees().stream()
                .filter(employee -> employee.getManagerName() != null
                        && employee.getManagerName().equalsIgnoreCase(request.getManagerName().trim()))
                .toList();
    }

    private List<Employee> getByProject(AIActionRequest request) {
        validateProjectName(request);
        return employeeService.getAllEmployees().stream()
                .filter(employee -> employee.getCurrentProjectName() != null
                        && employee.getCurrentProjectName().equalsIgnoreCase(request.getProjectName().trim()))
                .toList();
    }

    private void validateName(AIActionRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name is required for action: " + request.getAction());
        }
    }

    private void validateSalary(AIActionRequest request) {
        if (request.getSalary() == null || request.getSalary() <= 0) {
            throw new IllegalArgumentException("Valid salary is required for action: " + request.getAction());
        }
    }

    private void validateManagerName(AIActionRequest request) {
        if (request.getManagerName() == null || request.getManagerName().isBlank()) {
            throw new IllegalArgumentException("managerName is required for action: " + request.getAction());
        }
    }

    private void validateProjectName(AIActionRequest request) {
        if (request.getProjectName() == null || request.getProjectName().isBlank()) {
            throw new IllegalArgumentException("projectName is required for action: " + request.getAction());
        }
    }

    private EmployeeDto createDefaultEmployeeDto(String fullName, Integer salary) {
        EmployeeDto dto = new EmployeeDto();
        UserDto userDto = new UserDto();
        String normalized = fullName.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", ".");
        long unique = System.currentTimeMillis();
        userDto.setEmail(normalized + "." + unique + "@company.com");
        userDto.setPassword("Welcome@123");
        userDto.setRole("ROLE_EMPLOYEE");
        dto.setUser(userDto);

        dto.setManagerName("HR Assistant");
        dto.setCurrentProjectName("Unassigned");

        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setFullName(fullName.trim());
        personalDetails.setDateOfBirth(LocalDate.of(1995, 1, 1));
        personalDetails.setGender("Unknown");
        personalDetails.setAge(31);
        personalDetails.setCurrentAddress(defaultAddress());
        personalDetails.setPermanentAddress(defaultAddress());
        personalDetails.setMobile("9999999999");
        personalDetails.setPersonalEmail("personal." + unique + "@mail.com");
        personalDetails.setEmergencyContactName("Emergency Contact");
        personalDetails.setEmergencyContactMobile("8888888888");
        dto.setPersonalDetails(personalDetails);

        ProfessionalDetails professionalDetails = new ProfessionalDetails();
        professionalDetails.setEmploymentCode(String.valueOf((int) (100000 + (unique % 900000))));
        professionalDetails.setCompanyEmail(userDto.getEmail());
        professionalDetails.setOfficePhone("12345678");
        professionalDetails.setOfficeAddress(defaultAddress());
        professionalDetails.setReportingManagerEmployeeCode("100001");
        professionalDetails.setHrName("HR Assistant");
        professionalDetails.setDateOfJoining(LocalDate.now(ZoneOffset.UTC));
        professionalDetails.setEmploymentHistory(new ArrayList<>());
        dto.setProfessionalDetails(professionalDetails);

        dto.setProjects(new ArrayList<>());

        Finance finance = new Finance();
        finance.setPanCard("ABCDE1234F");
        finance.setAadharCard("123412341234");
        finance.setBankDetails(defaultBankDetails());
        finance.setCtcBreakup(String.valueOf(salary));
        dto.setFinance(finance);

        return dto;
    }

    private EmployeeDto mapEmployeeToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        UserDto userDto = new UserDto();
        userDto.setEmail(employee.getUser() != null ? employee.getUser().getEmail() : null);
        userDto.setPassword("Welcome@123");
        userDto.setRole(employee.getUser() != null ? employee.getUser().getRole() : "ROLE_EMPLOYEE");
        dto.setUser(userDto);
        dto.setManagerName(employee.getManagerName());
        dto.setCurrentProjectName(employee.getCurrentProjectName());
        dto.setPersonalDetails(employee.getPersonalDetails());
        dto.setProfessionalDetails(employee.getProfessionalDetails());
        dto.setProjects(employee.getProjects() != null ? employee.getProjects() : new ArrayList<>());
        dto.setFinance(employee.getFinance());
        return dto;
    }

    private Address defaultAddress() {
        Address address = new Address();
        address.setAddressLine1("NA");
        address.setAddressLine2("NA");
        address.setCity("NA");
        address.setPinCode("123456");
        return address;
    }

    private BankDetails defaultBankDetails() {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setBankName("NA");
        bankDetails.setBranch("NA");
        bankDetails.setIfscCode("SBIN0000001");
        return bankDetails;
    }

    private int extractSalary(Employee employee) {
        if (employee == null || employee.getFinance() == null || employee.getFinance().getCtcBreakup() == null) {
            return 0;
        }
        String salaryText = employee.getFinance().getCtcBreakup().replaceAll("[^0-9]", "");
        if (salaryText.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(salaryText);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
