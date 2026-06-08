package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Projeto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNome(String nome);
    List<Projeto> findAllByDataInicio(LocalDate dataInicio);
    List<Projeto> findAllByStatus(Projeto.StatusProjeto status);

}
