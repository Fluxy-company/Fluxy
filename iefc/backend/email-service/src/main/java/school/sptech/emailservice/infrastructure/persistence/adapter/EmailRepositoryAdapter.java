package school.sptech.emailservice.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.infrastructure.persistence.entity.EmailJpaEntity;
import school.sptech.emailservice.infrastructure.persistence.mapper.EmailPersistenceMapper;
import school.sptech.emailservice.infrastructure.persistence.repository.EmailJpaRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class EmailRepositoryAdapter implements EmailRepositoryPort {

    private final EmailJpaRepository jpaRepository;

    public EmailRepositoryAdapter(EmailJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Email salvar(Email email) {
        EmailJpaEntity salvo = jpaRepository.save(EmailPersistenceMapper.toEntity(email));
        return EmailPersistenceMapper.toDomain(salvo);
    }

    @Override
    public Optional<Email> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(EmailPersistenceMapper::toDomain);
    }
}
