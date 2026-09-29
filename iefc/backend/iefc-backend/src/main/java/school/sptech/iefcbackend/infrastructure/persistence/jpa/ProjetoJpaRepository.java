package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.Projeto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjetoJpaRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNome(String nome);
    List<Projeto> findAllByDataInicio(LocalDate dataInicio);
    List<Projeto> findAllByStatus(Projeto.StatusProjeto status);

}
