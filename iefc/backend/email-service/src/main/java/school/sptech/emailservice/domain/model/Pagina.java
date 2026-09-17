package school.sptech.emailservice.domain.model;

import java.util.List;

public record Pagina<T>(List<T> conteudo, int pagina, int tamanho, long totalElementos) {
}
