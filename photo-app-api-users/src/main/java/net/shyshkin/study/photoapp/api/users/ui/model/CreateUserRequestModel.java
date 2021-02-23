package net.shyshkin.study.photoapp.api.users.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestModel {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
