package net.shyshkin.study.app.ws.ui.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID userId;
    @NotBlank
    @Size(min = 3, max = 255)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 255)
    private String lastName;
    @NotBlank
    @Size(min = 3, max = 255)
    @Email
    private String email;
}
