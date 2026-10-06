package school.sptech.iefcbackend.web.dto.relatorio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public class MembroEquipeDTO {

    private String nome;
    private String cargo;
    private String bio;
    private String categoria;
    private MultipartFile foto;

    @JsonIgnore
    private String fotoBase64;

    public MembroEquipeDTO() {
    }

    public MembroEquipeDTO(String nome, String cargo, String bio, String categoria, MultipartFile foto, String fotoBase64) {
        this.nome = nome;
        this.cargo = cargo;
        this.bio = bio;
        this.categoria = categoria;
        this.foto = foto;
        this.fotoBase64 = fotoBase64;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public MultipartFile getFoto() {
        return foto;
    }

    public void setFoto(MultipartFile foto) {
        this.foto = foto;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}
