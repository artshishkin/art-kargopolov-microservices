package net.shyshkin.study.photoapp.api.users.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {

    private static final long serialVersionUID = -2167009431046432830L;

    private UUID userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String encryptedPassword;
}
