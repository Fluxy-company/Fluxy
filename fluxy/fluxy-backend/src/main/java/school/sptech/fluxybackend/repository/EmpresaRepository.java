package school.sptech.fluxybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.fluxybackend.models.Empresa;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);
}
