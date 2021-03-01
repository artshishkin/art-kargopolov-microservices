package net.shyshkin.study.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByEmail(String email);

    Optional<UserEntity> findOneByUserId(UUID userId);
}
