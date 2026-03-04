package school.sptech.fluxybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.fluxybackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
