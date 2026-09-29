package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Projeto;
import school.sptech.iefcbackend.domain.port.ProjetoRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.ProjetoJpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ProjetoRepositoryAdapter implements ProjetoRepositoryPort {

    private final ProjetoJpaRepository jpaRepository;

    public ProjetoRepositoryAdapter(ProjetoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Projeto save(Projeto projeto) {
        return jpaRepository.save(projeto);
    }

    @Override
    public List<Projeto> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Page<Projeto> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Projeto> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Projeto> findByNome(String nome) {
        return jpaRepository.findByNome(nome);
    }

    @Override
    public List<Projeto> findAllByDataInicio(LocalDate dataInicio) {
        return jpaRepository.findAllByDataInicio(dataInicio);
    }

    @Override
    public List<Projeto> findAllByStatus(Projeto.StatusProjeto status) {
        return jpaRepository.findAllByStatus(status);
    }

    @Override
    public void delete(Projeto projeto) {
        jpaRepository.delete(projeto);
    }
}
