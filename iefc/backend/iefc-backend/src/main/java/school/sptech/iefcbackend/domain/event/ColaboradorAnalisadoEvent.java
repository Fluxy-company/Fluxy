package school.sptech.iefcbackend.domain.event;

import org.springframework.context.ApplicationEvent;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;
import school.sptech.iefcbackend.domain.entity.Usuario;

public class ColaboradorAnalisadoEvent extends ApplicationEvent {

    private final Usuario colaborador;

    public ColaboradorAnalisadoEvent(Object source, Usuario colaborador) {
        super(source);
        this.colaborador = colaborador;
    }

    public Usuario getColaborador() {
        return colaborador;
    }

    public StatusCadastro getStatus() {
        return colaborador.getStatus();
    }
}
