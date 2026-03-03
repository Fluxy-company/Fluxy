package school.sptech.fluxybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.fluxybackend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCnpj(String cnpj);
}
