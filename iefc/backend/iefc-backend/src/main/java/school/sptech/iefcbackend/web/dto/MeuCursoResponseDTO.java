package school.sptech.iefcbackend.web.dto;

import java.time.LocalDate;

public record MeuCursoResponseDTO(
        Long cursoId,
        String titulo,
        String tema,
        String instrutor,
        Integer totalAulas,
        Integer aulasConcluidas,
        Long ultimoVideoId,
        LocalDate dataInscricao
) {
}
