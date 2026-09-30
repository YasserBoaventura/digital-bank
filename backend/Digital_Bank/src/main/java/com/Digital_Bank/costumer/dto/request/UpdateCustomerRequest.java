package com.Digital_Bank.costumer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateCustomerRequest(

        @Size(max = 150)
        String fullName,

        @Email(message = "Email inválido")
        String email,

        @Size(max = 30)
        String phone,

        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dateOfBirth
) {
}