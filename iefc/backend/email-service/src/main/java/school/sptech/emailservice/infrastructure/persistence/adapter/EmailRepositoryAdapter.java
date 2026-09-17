package school.sptech.emailservice.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.domain.model.Pagina;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.infrastructure.persistence.entity.EmailJpaEntity;
import school.sptech.emailservice.infrastructure.persistence.mapper.EmailPersistenceMapper;
import school.sptech.emailservice.infrastructure.persistence.repository.EmailJpaRepository;

import java.util.List;
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

    @Override
    public List<Email> buscarProntosParaEnvio(int limite) {
        List<EmailStatus> statusCandidatos = List.of(EmailStatus.PENDENTE, EmailStatus.FALHA);
        Pageable pageable = PageRequest.of(0, limite);

        return jpaRepository.findByStatusInOrderByCriadoEmAsc(statusCandidatos, pageable).stream()
                .map(EmailPersistenceMapper::toDomain)
                .filter(Email::podeReprocessar)
                .toList();
    }

    @Override
    public Pagina<Email> listar(EmailStatus status, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "criadoEm"));

        Page<EmailJpaEntity> page = (status != null)
                ? jpaRepository.findByStatus(status, pageable)
                : jpaRepository.findAll(pageable);

        List<Email> conteudo = page.getContent().stream()
                .map(EmailPersistenceMapper::toDomain)
                .toList();

        return new Pagina<>(conteudo, pagina, tamanho, page.getTotalElements());
    }
}
