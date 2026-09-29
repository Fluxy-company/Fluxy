package school.sptech.iefcbackend.usecase.projeto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Projeto;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.ProjetoRepositoryPort;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepositoryPort projetoRepositoryPort;

    public ProjetoService(ProjetoRepositoryPort projetoRepositoryPort) {
        this.projetoRepositoryPort = projetoRepositoryPort;
    }

    public Projeto salvarProjeto(Projeto projeto) {
        if (projeto.getEmpresa() == null || projeto.getEmpresa().getId() == null) {
            throw new RecursoNaoEncontradoException("Empresa não informada ou inválida");
        }
        return projetoRepositoryPort.save(projeto);
    }

    public List<Projeto> buscarTodos() {
        return projetoRepositoryPort.findAll();
    }

    public Page<Projeto> buscarTodosPaginado(Pageable pageable) {
        return projetoRepositoryPort.findAll(pageable);
    }

    public Projeto buscarProjetoPorNome(String nome) {
        return projetoRepositoryPort.findByNome(nome).orElseThrow(
                () -> new RecursoNaoEncontradoException("Não há nenhum projeto com este nome.")
        );
    }

    public List<Projeto> buscarProjetoPorDataInicio(LocalDate dataInicio) {
        List<Projeto> projetos = projetoRepositoryPort.findAllByDataInicio(dataInicio);

        if (projetos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum projeto foi iniciado na data informada.");
        }

        return projetos;
    }

    public List<Projeto> buscarProjetoPorStatus(String status) {
        Projeto.StatusProjeto statusEnum;
        try {
            statusEnum = Projeto.StatusProjeto.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        List<Projeto> projetos = projetoRepositoryPort.findAllByStatus(statusEnum);

        if (projetos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com o status informado.");
        }

        return projetos;
    }

    public Projeto atualizarPorId(Long id, Projeto projeto) {
        Projeto projetoEntity = projetoRepositoryPort.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com essa identificação.")
        );

        projetoEntity.setNome(projeto.getNome());
        projetoEntity.setDescricao(projeto.getDescricao());
        projetoEntity.setDataInicio(projeto.getDataInicio());
        projetoEntity.setDataFim(projeto.getDataFim());
        projetoEntity.setStatus(projeto.getStatus());

        return projetoRepositoryPort.save(projetoEntity);
    }

    public void deletarPorId(Long id) {
        Projeto projetoEntity = projetoRepositoryPort.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com essa identificação.")
        );
        projetoRepositoryPort.delete(projetoEntity);
    }
}
