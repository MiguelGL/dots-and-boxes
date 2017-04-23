package com.mgl.dotsandboxes.server.model.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(of = "email")
public class PlayerProfile {

    @NotBlank
    private final String name;

    @NotBlank
    @Email(regexp = ".+@.+\\..+")
    private final String email;

}
