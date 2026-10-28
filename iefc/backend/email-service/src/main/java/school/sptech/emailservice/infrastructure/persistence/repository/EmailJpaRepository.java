package school.sptech.emailservice.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.emailservice.infrastructure.persistence.entity.EmailJpaEntity;

import java.util.UUID;

public interface EmailJpaRepository extends JpaRepository<EmailJpaEntity, UUID> {
}
