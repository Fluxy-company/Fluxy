package school.sptech.iefcbackend.domain.port;

import school.sptech.iefcbackend.domain.entity.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepositoryPort {

    Empresa save(Empresa empresa);

    List<Empresa> findAll();

    Optional<Empresa> findById(Long id);

    Optional<Empresa> findByCnpj(String cnpj);

    void delete(Empresa empresa);
}
