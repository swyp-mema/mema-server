package com.swyp.mema.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserReq {

    private String username; //유저 id

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Nickname is required.")
    @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters.")
    private String nickname;
    private Long puzzleId;
    private String puzzleColor;
    private String role;
    private Long badgeCount;

}
