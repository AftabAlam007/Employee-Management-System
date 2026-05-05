package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.EmployeeDto;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import com.example.employeemanagementsystem.repository.UserRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// ✨✨ ADD ALL THESE IMPORTS ✨✨
import com.example.employeemanagementsystem.model.BankDetails;
import com.example.employeemanagementsystem.model.EmploymentHistory;
import com.example.employeemanagementsystem.model.Finance;
import com.example.employeemanagementsystem.model.PersonalDetails;
import com.example.employeemanagementsystem.model.ProfessionalDetails;
import com.example.employeemanagementsystem.model.Project;
// ✨✨ END OF IMPORTS ✨✨

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("employeeServiceImpl")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    // ✨✨ YOUR ERRORS WERE HERE. NOW THE FILE CAN FIND THESE CLASSES. ✨✨
    private void validateDates(List<EmploymentHistory> employmentHistoryList, List<Project> projectList) {
        if (employmentHistoryList != null) {
            for (EmploymentHistory history : employmentHistoryList) {
                if (history.getJoiningDate() != null && history.getEndDate() != null && history.getEndDate().isBefore(history.getJoiningDate())) {
                    throw new IllegalArgumentException("Employment history end date cannot be before joining date.");
                }
            }
        }
        if (projectList != null) {
            for (Project project : projectList) {
                if (project.getStartDate() != null && project.getEndDate() != null && project.getEndDate().isBefore(project.getStartDate())) {
                    throw new IllegalArgumentException("Project end date cannot be before start date.");
                }
            }
        }
    }

    @Override
    public Employee createEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();

        // Handle User creation/association
        if (employeeDto.getUser() != null) {
            User user = userRepository.findByEmail(employeeDto.getUser().getEmail()).orElse(null);
            if (user == null) {
                user = new User();
                user.setEmail(employeeDto.getUser().getEmail());
                user.setPassword(passwordEncoder.encode(employeeDto.getUser().getPassword())); // Hash password
                user.setRole(employeeDto.getUser().getRole());
                user = userRepository.save(user);
            }
            employee.setUser(user);
        }

        // Set basic employee details
        employee.setManagerName(employeeDto.getManagerName());
        employee.setCurrentProjectName(employeeDto.getCurrentProjectName());

        // Set Personal Details
        if (employeeDto.getPersonalDetails() != null) {
            employee.setPersonalDetails(employeeDto.getPersonalDetails());
        }

        // Set Professional Details
        if (employeeDto.getProfessionalDetails() != null) {
            employee.setProfessionalDetails(employeeDto.getProfessionalDetails());
        }

        // Set Projects
        if (employeeDto.getProjects() != null && !employeeDto.getProjects().isEmpty()) {
            employee.setProjects(employeeDto.getProjects());
        }

        // Set Finance Details
        if (employeeDto.getFinance() != null) {
            employee.setFinance(employeeDto.getFinance());
        }

        // Perform cross-field date validation
        validateDates(
                employeeDto.getProfessionalDetails() != null ? employeeDto.getProfessionalDetails().getEmploymentHistory() : Collections.emptyList(),
                employeeDto.getProjects()
        );

        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        if (id == null) {
            return null;
        }
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        if (id == null) {
            return null;
        }
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee != null) {
            // Update basic employee details (admin can update these)
            existingEmployee.setManagerName(employeeDto.getManagerName());
            existingEmployee.setCurrentProjectName(employeeDto.getCurrentProjectName());

            // Update Personal Details
            if (employeeDto.getPersonalDetails() != null) {
                PersonalDetails existingPersonalDetails = existingEmployee.getPersonalDetails();
                if (existingPersonalDetails == null) {
                    existingPersonalDetails = new PersonalDetails();
                    existingEmployee.setPersonalDetails(existingPersonalDetails);
                }
                // Admin Update Restriction: Admin should not have the provision to update Employment Code, Gender, and Date of Birth.
                // These fields are retained from the existing personal details.
                existingPersonalDetails.setFullName(employeeDto.getPersonalDetails().getFullName());
                // Admin cannot update Date of Birth, retain existing value
                // existingPersonalDetails.setDateOfBirth(employeeDto.getPersonalDetails().getDateOfBirth());
                // Admin cannot update Gender, retain existing value
                // existingPersonalDetails.setGender(employeeDto.getPersonalDetails().getGender());
                existingPersonalDetails.setAge(employeeDto.getPersonalDetails().getAge());
                existingPersonalDetails.setCurrentAddress(employeeDto.getPersonalDetails().getCurrentAddress());
                existingPersonalDetails.setPermanentAddress(employeeDto.getPersonalDetails().getPermanentAddress());
                existingPersonalDetails.setMobile(employeeDto.getPersonalDetails().getMobile());
                existingPersonalDetails.setPersonalEmail(employeeDto.getPersonalDetails().getPersonalEmail());
                existingPersonalDetails.setEmergencyContactName(employeeDto.getPersonalDetails().getEmergencyContactName());
                existingPersonalDetails.setEmergencyContactMobile(employeeDto.getPersonalDetails().getEmergencyContactMobile());
            }

            // Update Professional Details
            if (employeeDto.getProfessionalDetails() != null) {
                ProfessionalDetails existingProfessionalDetails = existingEmployee.getProfessionalDetails();
                if (existingProfessionalDetails == null) {
                    existingProfessionalDetails = new ProfessionalDetails();
                    existingEmployee.setProfessionalDetails(existingProfessionalDetails);
                }
                // Admin Update Restriction: Admin should not have the provision to update Employment Code, Company mail, and Date of Joining.
                // These fields are retained from the existing professional details.
                // Admin cannot update Employment Code, retain existing value
                // existingProfessionalDetails.setEmploymentCode(employeeDto.getProfessionalDetails().getEmploymentCode());
                // Admin cannot update Company email, retain existing value
                // existingProfessionalDetails.setCompanyEmail(employeeDto.getProfessionalDetails().getCompanyEmail());
                existingProfessionalDetails.setOfficePhone(employeeDto.getProfessionalDetails().getOfficePhone());
                existingProfessionalDetails.setOfficeAddress(employeeDto.getProfessionalDetails().getOfficeAddress());
                existingProfessionalDetails.setReportingManagerEmployeeCode(employeeDto.getProfessionalDetails().getReportingManagerEmployeeCode());
                existingProfessionalDetails.setHrName(employeeDto.getProfessionalDetails().getHrName());
                // Admin cannot update Date of Joining, retain existing value
                // existingProfessionalDetails.setDateOfJoining(employeeDto.getProfessionalDetails().getDateOfJoining());
                // Update employment history
                existingProfessionalDetails.getEmploymentHistory().clear();
                if (employeeDto.getProfessionalDetails().getEmploymentHistory() != null) {
                    existingProfessionalDetails.getEmploymentHistory().addAll(employeeDto.getProfessionalDetails().getEmploymentHistory());
                }
            }

            // Update Projects
            if (employeeDto.getProjects() != null) {
                existingEmployee.getProjects().clear();
                existingEmployee.getProjects().addAll(employeeDto.getProjects());
            }

            // Update Finance Details
            if (employeeDto.getFinance() != null) {
                Finance existingFinance = existingEmployee.getFinance();
                if (existingFinance == null) {
                    existingFinance = new Finance();
                    existingEmployee.setFinance(existingFinance);
                }
                existingFinance.setPanCard(employeeDto.getFinance().getPanCard());
                existingFinance.setAadharCard(employeeDto.getFinance().getAadharCard());
                existingFinance.setCtcBreakup(employeeDto.getFinance().getCtcBreakup());
                // Update Bank Details
                if (employeeDto.getFinance().getBankDetails() != null) {
                    BankDetails existingBankDetails = existingFinance.getBankDetails();
                    if (existingBankDetails == null) {
                        existingBankDetails = new BankDetails();
                        existingFinance.setBankDetails(existingBankDetails);
                    }
                    existingBankDetails.setBankName(employeeDto.getFinance().getBankDetails().getBankName());
                    existingBankDetails.setBranch(employeeDto.getFinance().getBankDetails().getBranch());
                    existingBankDetails.setIfscCode(employeeDto.getFinance().getBankDetails().getIfscCode());
                }
            }

            

            // Perform cross-field date validation
            validateDates(
                    existingEmployee.getProfessionalDetails() != null ? existingEmployee.getProfessionalDetails().getEmploymentHistory() : Collections.emptyList(),
                    existingEmployee.getProjects()
            );

            return employeeRepository.save(existingEmployee);
        }
        return null;
    }

    @Override
    public void deleteEmployee(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public boolean isEmployeeOwner(String username, Long employeeId) {
        if (employeeId == null) {
            return false;
        }
        return employeeRepository.findById(employeeId)
                .map(employee -> employee.getUser() != null && employee.getUser().getEmail().equals(username))
                .orElse(false);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByUserEmail(email).orElse(null);
    }

    @Override
    public Optional<Employee> getEmployeeByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return employeeRepository.findFirstByPersonalDetailsFullNameIgnoreCase(name.trim());
    }

    @Override
    public Employee getHighestSalaryEmployee() {
        return employeeRepository.findAll()
                .stream()
                .max(Comparator.comparingInt(this::extractSalary))
                .orElse(null);
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

    @Override
    public byte[] downloadPayslip(Long employeeId, String username) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee ID cannot be null for downloading payslip.");
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add Payslip Header
            document.add(new Paragraph("----------------------------------------------------"));
            document.add(new Paragraph("                  PAYSLIP - " + employee.getPersonalDetails().getFullName().toUpperCase()));
            document.add(new Paragraph("----------------------------------------------------"));
            document.add(new Paragraph(" "));

            // Add Personal Details
            document.add(new Paragraph("Personal Details:"));
            document.add(new Paragraph("  Full Name: " + employee.getPersonalDetails().getFullName()));
            document.add(new Paragraph("  Employment Code: " + employee.getProfessionalDetails().getEmploymentCode()));
            document.add(new Paragraph("  Date of Birth: " + employee.getPersonalDetails().getDateOfBirth()));
            document.add(new Paragraph("  Gender: " + employee.getPersonalDetails().getGender()));
            document.add(new Paragraph("  Personal Email: " + employee.getPersonalDetails().getPersonalEmail()));
            document.add(new Paragraph("  Mobile: " + employee.getPersonalDetails().getMobile()));
            document.add(new Paragraph(" "));

            // Add Professional Details
            document.add(new Paragraph("Professional Details:"));
            document.add(new Paragraph("  Company Email: " + employee.getProfessionalDetails().getCompanyEmail()));
            document.add(new Paragraph("  Date of Joining: " + employee.getProfessionalDetails().getDateOfJoining()));
            document.add(new Paragraph("  Manager Name: " + employee.getManagerName()));
            document.add(new Paragraph("  HR Name: " + employee.getProfessionalDetails().getHrName()));
            document.add(new Paragraph(" "));

            // Add Finance Details
            document.add(new Paragraph("Finance Details:"));
            document.add(new Paragraph("  PAN Card: " + employee.getFinance().getPanCard()));
            document.add(new Paragraph("  Aadhar Card: " + employee.getFinance().getAadharCard()));
            if (employee.getFinance().getBankDetails() != null) {
                document.add(new Paragraph("  Bank Name: " + employee.getFinance().getBankDetails().getBankName()));
                document.add(new Paragraph("  Branch: " + employee.getFinance().getBankDetails().getBranch()));
                document.add(new Paragraph("  IFSC Code: " + employee.getFinance().getBankDetails().getIfscCode()));
            }
            document.add(new Paragraph("  CTC Breakup: " + employee.getFinance().getCtcBreakup()));
            document.add(new Paragraph(" "));

            // Add a note about payslip period (dummy for now)
            document.add(new Paragraph("Note: This payslip is for the period of [Month Year] (Placeholder)."));
            document.add(new Paragraph("Generated on: " + LocalDate.now()));

            document.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            // Consider throwing a custom exception or logging more effectively
            throw new RuntimeException("Error generating payslip for employee ID: " + employeeId, e);
        }
    }
}