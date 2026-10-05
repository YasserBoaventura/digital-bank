package com.Digital_Bank.costumer.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateCustomerRequest(

    @NotBlank(message = "Full name is required.")
    @Size(max = 150)
    String fullName,

    @NotBlank(message = "Email is required")
    @Email(message = "Email invalid")
    String email,

    @NotBlank(message = "phone number is required")
    @Size(max = 30)
    String phone,

    @NotBlank(message = "number document is required")
    @Size(max = 50)
    String documentNumber,

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past.")
    LocalDate dateOfBirth
) {

}
