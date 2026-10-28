package school.sptech.iefcbackend.domain.port;

import school.sptech.iefcbackend.domain.entity.Usuario;

public interface NotificacaoColaboradorPort {

    void notificarNovoCadastroPendente(Usuario colaborador);

    void notificarCadastroAprovado(Usuario colaborador);

    void notificarCadastroReprovado(Usuario colaborador);
}

