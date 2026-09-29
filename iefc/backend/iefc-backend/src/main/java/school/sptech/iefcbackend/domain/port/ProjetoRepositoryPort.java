package school.sptech.iefcbackend.domain.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import school.sptech.iefcbackend.domain.entity.Projeto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjetoRepositoryPort {

    Projeto save(Projeto projeto);

    List<Projeto> findAll();

    Page<Projeto> findAll(Pageable pageable);

    Optional<Projeto> findById(Long id);

    Optional<Projeto> findByNome(String nome);

    List<Projeto> findAllByDataInicio(LocalDate dataInicio);

    List<Projeto> findAllByStatus(Projeto.StatusProjeto status);

    void delete(Projeto projeto);
}
