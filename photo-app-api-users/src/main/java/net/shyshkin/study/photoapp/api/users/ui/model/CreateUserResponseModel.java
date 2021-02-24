package net.shyshkin.study.photoapp.api.users.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponseModel {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
}
