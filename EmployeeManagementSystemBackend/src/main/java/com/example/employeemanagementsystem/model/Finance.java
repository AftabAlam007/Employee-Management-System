package com.example.employeemanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Finance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "PAN card cannot be empty")
    private String panCard;
    @NotBlank(message = "Aadhar card cannot be empty")
    private String aadharCard;

    @Valid
    @NotNull(message = "Bank details cannot be null")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_details_id")
    @JsonManagedReference
    private BankDetails bankDetails;

    @NotBlank(message = "CTC breakup cannot be empty")
    private String ctcBreakup; // This could be a JSON string or a separate entity
}
