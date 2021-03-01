package net.shyshkin.study.photoapp.api.users.ui.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseModel {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;

    @Singular
    private List<AlbumResponseModel> albums;

}
