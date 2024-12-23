package com.swyp.mema.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "비밀번호 변경")
public class UpdatePasswordReq {

    @Schema(description = "비밀번호", example = "asdf1234")
    private String password;

}
