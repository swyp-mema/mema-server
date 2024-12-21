package com.swyp.mema.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class EmailReq {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;
}
