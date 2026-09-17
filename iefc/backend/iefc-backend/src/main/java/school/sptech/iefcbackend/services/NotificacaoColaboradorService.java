package school.sptech.iefcbackend.services;

import school.sptech.iefcbackend.models.Usuario;

public interface NotificacaoColaboradorService {

    void notificarNovoCadastroPendente(Usuario colaborador);

    void notificarCadastroAprovado(Usuario colaborador);

    void notificarCadastroReprovado(Usuario colaborador);
}
