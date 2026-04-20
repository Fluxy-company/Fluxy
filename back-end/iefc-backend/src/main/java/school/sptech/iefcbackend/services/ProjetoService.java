package school.sptech.iefcbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Projeto;
import school.sptech.iefcbackend.repository.ProjetoRepository;

import java.sql.Date;
import java.util.List;

@Service
public class ProjetoService {

    @Autowired
    ProjetoRepository projetoRepository;

    public void salvarProjeto(Projeto projeto) {
        projetoRepository.save(projeto);
    }

    public List<Projeto> buscarTodos() { return projetoRepository.findAll(); }

    public Projeto buscarProjetoPorNome(String nome){
        return projetoRepository.findByNome(nome).orElseThrow(
            () -> new RecursoNaoEncontradoException("Não há nenhum projeto com este nome.")
        );
    }

    public List<Projeto> buscarProjetoPorDataInicio(Date dataInicio){
        List<Projeto> projetos = projetoRepository.findAllByDataInicio(dataInicio);

        if (projetos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum projeto foi iniciado na data informada.");
        }

        return projetos;
    }

    public List<Projeto> buscarProjetoPorStatus(String status){
        List<Projeto> projetos = projetoRepository.findAllByStatus(status);

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
