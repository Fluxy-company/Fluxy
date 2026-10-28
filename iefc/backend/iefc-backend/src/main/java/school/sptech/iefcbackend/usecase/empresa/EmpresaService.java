package school.sptech.iefcbackend.usecase.empresa;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Empresa;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.EmpresaRepositoryPort;
import school.sptech.iefcbackend.domain.port.UsuarioRepositoryPort;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepositoryPort empresaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public EmpresaService(EmpresaRepositoryPort empresaRepositoryPort,
                          UsuarioRepositoryPort usuarioRepositoryPort) {
        this.empresaRepositoryPort = empresaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public void salvarEmpresa(Empresa empresa) {
        empresa.setUsuario(buscarUsuarioValido(empresa.getUsuario()));
        empresaRepositoryPort.save(empresa);
    }

    public List<Empresa> buscarTodos() {
        return empresaRepositoryPort.findAll();
    }

    public Empresa buscarEmpresaPorCnpj(String cnpj) {
        return empresaRepositoryPort.findByCnpj(cnpj).orElseThrow(
                () -> new RecursoNaoEncontradoException("Cnpj não encontrado!")
        );
    }

    public void atualizarPorId(Long id, Empresa empresa) {
        Empresa empresaEntity = empresaRepositoryPort.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Empresa não encontrada"));
        empresaEntity.setNome(empresa.getNome());
        empresaEntity.setCnpj(empresa.getCnpj());
        empresaEntity.setTelefone(empresa.getTelefone());
        empresaEntity.setUsuario(buscarUsuarioValido(empresa.getUsuario()));

        empresaRepositoryPort.save(empresaEntity);
    }

    public void deletarPorId(Long id) {
        Empresa entity = empresaRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
        empresaRepositoryPort.delete(entity);
    }

    private Usuario buscarUsuarioValido(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            return null;
        }
        Long idUsuario = usuario.getId();
        if (idUsuario <= 0) {
            throw new RecursoNaoEncontradoException("Usuario invalido para vinculo na empresa");
        }
        return usuarioRepositoryPort.findById(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado para vincular na empresa"));
    }
}
