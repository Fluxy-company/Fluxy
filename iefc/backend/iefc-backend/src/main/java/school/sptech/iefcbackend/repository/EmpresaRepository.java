package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Empresa;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);
}
