package school.sptech.iefcbackend.usecase.inscricao;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Curso;
import school.sptech.iefcbackend.domain.entity.Inscricao;
import school.sptech.iefcbackend.domain.entity.ProgressoAula;
import school.sptech.iefcbackend.domain.entity.Video;
import school.sptech.iefcbackend.domain.port.InscricaoRepositoryPort;
import school.sptech.iefcbackend.domain.port.ProgressoAulaRepositoryPort;
import school.sptech.iefcbackend.domain.port.VideoRepositoryPort;
import school.sptech.iefcbackend.web.dto.MeuCursoResponseDTO;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class MeusCursosService {

    private final InscricaoRepositoryPort inscricaoRepositoryPort;
    private final ProgressoAulaRepositoryPort progressoAulaRepositoryPort;
    private final VideoRepositoryPort videoRepositoryPort;

    public MeusCursosService(InscricaoRepositoryPort inscricaoRepositoryPort,
                             ProgressoAulaRepositoryPort progressoAulaRepositoryPort,
                             VideoRepositoryPort videoRepositoryPort) {
        this.inscricaoRepositoryPort = inscricaoRepositoryPort;
        this.progressoAulaRepositoryPort = progressoAulaRepositoryPort;
        this.videoRepositoryPort = videoRepositoryPort;
    }

    public List<MeuCursoResponseDTO> listar(Long usuarioId) {
        return inscricaoRepositoryPort.findByUsuarioId(usuarioId).stream()
                .map(inscricao -> montar(usuarioId, inscricao))
                .toList();
    }

    private MeuCursoResponseDTO montar(Long usuarioId, Inscricao inscricao) {
        Curso curso = inscricao.getCurso();
        List<Video> aulas = videoRepositoryPort.findByCursoIdOrderByOrdem(curso.getId());
        List<ProgressoAula> progressos = progressoAulaRepositoryPort
                .findByUsuarioIdAndVideoCursoId(usuarioId, curso.getId());

        int concluidas = (int) progressos.stream()
                .filter(p -> Boolean.TRUE.equals(p.getConcluida()))
                .count();

        Long ultimoVideoId = progressos.stream()
                .filter(p -> p.getDataAtualizacao() != null)
                .max(Comparator.comparing(ProgressoAula::getDataAtualizacao))
                .map(p -> p.getVideo().getId())
                .orElse(aulas.isEmpty() ? null : aulas.get(0).getId());

        return new MeuCursoResponseDTO(
                curso.getId(),
                curso.getTitulo(),
                curso.getTema() != null ? curso.getTema().getNome() : null,
                curso.getInstrutor(),
                aulas.size(),
                concluidas,
                ultimoVideoId,
                inscricao.getDataInscricao()
        );
    }
}
