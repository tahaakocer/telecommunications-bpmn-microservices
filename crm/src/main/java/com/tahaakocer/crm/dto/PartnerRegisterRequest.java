package com.tahaakocer.crm.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PartnerRegisterRequest {

    @NotNull(message = "TC No boş olamaz")
    @Size(min = 11, max = 11, message = "TC No 11 haneli olmalıdır")
    @Pattern(regexp = "^[0-9]+$", message = "TC No sadece rakamlardan oluşmalıdır")
    private String tckn;

    @NotBlank(message = "Ad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Ad 2-50 karakter arasında olmalıdır")
    private String firstName;

    @NotBlank(message = "Soyad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Soyad 2-50 karakter arasında olmalıdır")
    private String lastName;

    @NotNull(message = "Doğum yılı boş olamaz")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Geçerli bir doğum yılı giriniz (1900-2099)")
    private Integer birthYear;

    @Pattern(regexp = "^[0-9]{10}$", message = "Telefon numarası 10 haneli olmalıdır")
    private String phoneNumber;

    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String password;
}