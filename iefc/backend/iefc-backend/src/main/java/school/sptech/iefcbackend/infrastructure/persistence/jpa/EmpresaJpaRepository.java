package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.Empresa;

import java.util.Optional;

public interface EmpresaJpaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);
}
