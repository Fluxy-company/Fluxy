package school.sptech.fluxybackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.fluxybackend.exception.RecursoNaoEncontradoException;
import school.sptech.fluxybackend.models.Empresa;
import school.sptech.fluxybackend.models.Usuario;
import school.sptech.fluxybackend.repository.EmpresaRepository;
import school.sptech.fluxybackend.repository.UsuarioRepository;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public void salvarEmpresa(Empresa empresa){
        empresa.setUsuario(buscarUsuarioValido(empresa.getUsuario()));
        empresaRepository.save(empresa);
    }

    public List<Empresa> buscarTodos() {
        return empresaRepository.findAll();
    }

    public Empresa buscarEmpresaPorCnpj(String cnpj){
        return empresaRepository.findByCnpj(cnpj).orElseThrow(
        () -> new RecursoNaoEncontradoException("Cnpj  não encontrado!")
        );
    }

    public void atualizarPorId(Long id, Empresa empresa){
        Empresa empresaEntity = empresaRepository.findById(id).orElseThrow(()
        -> new RecursoNaoEncontradoException("Empresa não encontrada"));
        empresaEntity.setNome(empresa.getNome());
        empresaEntity.setCnpj(empresa.getCnpj());
        empresaEntity.setTelefone(empresa.getTelefone());
        empresaEntity.setUsuario(buscarUsuarioValido(empresa.getUsuario()));

        empresaRepository.save(empresaEntity);
    }

    public void deletarPorId(Long id){
        Empresa entity = empresaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
        empresaRepository.delete(entity);
    }

    private Usuario buscarUsuarioValido(Usuario usuario){
        if (usuario == null || usuario.getIdUsuario() == null) {
            return null;
        }
        Long idUsuario = usuario.getIdUsuario();
        if (idUsuario <= 0) {
            throw new RecursoNaoEncontradoException("Usuario invalido para vinculo na empresa");
        }
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado para vincular na empresa"));
    }
}
