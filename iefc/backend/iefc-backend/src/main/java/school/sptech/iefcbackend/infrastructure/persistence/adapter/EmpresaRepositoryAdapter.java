package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Empresa;
import school.sptech.iefcbackend.domain.port.EmpresaRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.EmpresaJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class EmpresaRepositoryAdapter implements EmpresaRepositoryPort {

    private final EmpresaJpaRepository jpaRepository;

    public EmpresaRepositoryAdapter(EmpresaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Empresa save(Empresa empresa) {
        return jpaRepository.save(empresa);
    }

    @Override
    public List<Empresa> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Empresa> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Empresa> findByCnpj(String cnpj) {
        return jpaRepository.findByCnpj(cnpj);
    }

    @Override
    public void delete(Empresa empresa) {
        jpaRepository.delete(empresa);
    }
}
