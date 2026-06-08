package school.sptech.iefcbackend.services;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Projeto;
import school.sptech.iefcbackend.repository.ProjetoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public Projeto salvarProjeto(Projeto projeto) {
        if (projeto.getEmpresa() == null || projeto.getEmpresa().getId() == null) {
            throw new RecursoNaoEncontradoException("Empresa não informada ou inválida");
        }
        return projetoRepository.save(projeto);
    }

    public List<Projeto> buscarTodos() { return projetoRepository.findAll(); }

    public Projeto buscarProjetoPorNome(String nome){
        return projetoRepository.findByNome(nome).orElseThrow(
            () -> new RecursoNaoEncontradoException("Não há nenhum projeto com este nome.")
        );
    }

    public List<Projeto> buscarProjetoPorDataInicio(LocalDate dataInicio){
        List<Projeto> projetos = projetoRepository.findAllByDataInicio(dataInicio);

        if (projetos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum projeto foi iniciado na data informada.");
        }

        return projetos;
    }

    public List<Projeto> buscarProjetoPorStatus(String status){
        Projeto.StatusProjeto statusEnum;
        try {
            statusEnum = Projeto.StatusProjeto.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        List<Projeto> projetos = projetoRepository.findAllByStatus(statusEnum);

        if (projetos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com o status informado.");
        }

        return projetos;
    }

    public Projeto atualizarPorId(Long id, Projeto projeto){
        Projeto projetoEntity = projetoRepository.findById(id).orElseThrow(
            () -> new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com essa identificação.")
        );

        projetoEntity.setNome(projeto.getNome());
        projetoEntity.setDescricao(projeto.getDescricao());
        projetoEntity.setDataInicio(projeto.getDataInicio());
        projetoEntity.setDataFim(projeto.getDataFim());
        projetoEntity.setStatus(projeto.getStatus());

        return projetoRepository.save(projetoEntity);
    }

    public void deletarPorId(Long id){
        Projeto projetoEntity = projetoRepository.findById(id).orElseThrow(
            () -> new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com essa identificação.")
        );
        projetoRepository.delete(projetoEntity);
    }
}
