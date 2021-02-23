package net.shyshkin.study.photoapp.api.users.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestModel {

    @NotBlank
    @Size(min = 3, max = 255, message = "First Name must be from 3 to 255 characters long")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 255, message = "Last Name must be from 3 to 255 characters long")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).{6,255}$",
            message = "Password must be from 6 to 255 symbols long and have at least one number and a Capital letter")
    private String password;

    @NotBlank
    @Email
    private String email;
}
