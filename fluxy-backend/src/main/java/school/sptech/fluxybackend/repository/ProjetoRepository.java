package school.sptech.fluxybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.fluxybackend.models.Projeto;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNome(String nome);
    List<Projeto> findAllByDataInicio(Date dataInicio);
    List<Projeto> findAllByStatus(String status);

}
