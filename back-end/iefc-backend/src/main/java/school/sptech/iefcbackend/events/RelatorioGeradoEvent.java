package school.sptech.iefcbackend.events;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.time.LocalDateTime;

public class RelatorioGeradoEvent extends ApplicationEvent {

    private final String ano;
    private final long tamanhoBytes;
    private final LocalDateTime geradoEm;

    public RelatorioGeradoEvent(Object source, String ano, long tamanhoBytes) {
        super(source);
        this.ano = ano;
        this.tamanhoBytes = tamanhoBytes;
        this.geradoEm = LocalDateTime.now();
    }

    public String getAno() {
        return ano;
    }

    public long getTamanhoBytes() {
        return tamanhoBytes;
    }

    public LocalDateTime getGeradoEm() {
        return geradoEm;
    }
}
