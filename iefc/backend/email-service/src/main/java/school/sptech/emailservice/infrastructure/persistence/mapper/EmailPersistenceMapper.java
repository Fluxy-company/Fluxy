package school.sptech.emailservice.infrastructure.persistence.mapper;

import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.infrastructure.persistence.entity.EmailJpaEntity;

import java.util.Arrays;
import java.util.List;

public final class EmailPersistenceMapper {

    private static final String SEPARADOR = ";";

    private EmailPersistenceMapper() {
    }

    public static EmailJpaEntity toEntity(Email email) {
        EmailJpaEntity entity = new EmailJpaEntity();
        entity.setId(email.getId());
        entity.setDestinatarios(juntar(email.getDestinatarios()));
        entity.setCopia(juntar(email.getCopia()));
        entity.setRemetente(email.getRemetente());
        entity.setAssunto(email.getAssunto());
        entity.setCorpo(email.getCorpo());
        entity.setStatus(email.getStatus());
        entity.setTentativas(email.getTentativas());
        entity.setMaxTentativas(email.getMaxTentativas());
        entity.setCriadoEm(email.getCriadoEm());
        entity.setAtualizadoEm(email.getAtualizadoEm());
        entity.setEnviadoEm(email.getEnviadoEm());
        entity.setMensagemErro(email.getMensagemErro());
        return entity;
    }

    public static Email toDomain(EmailJpaEntity entity) {
        return Email.reconstruir(
                entity.getId(),
                separar(entity.getDestinatarios()),
                separar(entity.getCopia()),
                entity.getRemetente(),
                entity.getAssunto(),
                entity.getCorpo(),
                entity.getStatus(),
                entity.getTentativas(),
                entity.getMaxTentativas(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getEnviadoEm(),
                entity.getMensagemErro()
        );
    }

    private static String juntar(List<String> valores) {
        if (valores == null || valores.isEmpty()) {
            return null;
        }
        return String.join(SEPARADOR, valores);
    }

    private static List<String> separar(String valor) {
        if (valor == null || valor.isBlank()) {
            return List.of();
        }
        return Arrays.stream(valor.split(SEPARADOR))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
