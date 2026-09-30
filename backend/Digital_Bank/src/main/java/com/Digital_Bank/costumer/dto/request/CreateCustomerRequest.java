package com.Digital_Bank.costumer.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateCustomerRequest(

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 150)
    String fullName,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 30)
    String phone,

    @NotBlank(message = "Número do documento é obrigatório")
    @Size(max = 50)
    String documentNumber,

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve estar no passado")
    LocalDate dateOfBirth
) {

}
