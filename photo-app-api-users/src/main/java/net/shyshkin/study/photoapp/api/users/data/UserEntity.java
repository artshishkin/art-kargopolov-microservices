package net.shyshkin.study.photoapp.api.users.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -2080606379981604584L;

    @Id
    @GeneratedValue
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(length = 36, columnDefinition = "varchar(36)", nullable = false, unique = true)
    @Type(type = "uuid-char")
    private UUID userId;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;
}
