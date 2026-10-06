package school.sptech.iefcbackend.domain.event;

import org.springframework.context.ApplicationEvent;
import school.sptech.iefcbackend.domain.entity.Usuario;

public class ColaboradorCadastradoEvent extends ApplicationEvent {

    private final Usuario colaborador;

    public ColaboradorCadastradoEvent(Object source, Usuario colaborador) {
        super(source);
        this.colaborador = colaborador;
    }

    public Usuario getColaborador() {
        return colaborador;
    }
}
