package school.sptech.emailservice.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.infrastructure.persistence.entity.EmailJpaEntity;

import java.util.List;
import java.util.UUID;

public interface EmailJpaRepository extends JpaRepository<EmailJpaEntity, UUID> {

    Page<EmailJpaEntity> findByStatus(EmailStatus status, Pageable pageable);

    List<EmailJpaEntity> findByStatusInOrderByCriadoEmAsc(List<EmailStatus> status, Pageable pageable);
}
