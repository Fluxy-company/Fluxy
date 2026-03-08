package school.sptech.fluxybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

        private Long id;
        private String nome;
        private String sobrenome;
        private String email;
        private String senha;
        private String telefone;
        private Long createdAt;

    }
