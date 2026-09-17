package school.sptech.iefcbackend.events;

import org.springframework.context.ApplicationEvent;
import school.sptech.iefcbackend.models.StatusCadastro;
import school.sptech.iefcbackend.models.Usuario;

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
