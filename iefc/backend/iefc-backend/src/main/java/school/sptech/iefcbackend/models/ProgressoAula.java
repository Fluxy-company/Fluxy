package school.sptech.iefcbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "tb_progresso_aula")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progresso_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_video")
    private Video video;

    @Column(name = "concluida")
    private Boolean concluida;

    @Column(name = "anotacao", length = 2000)
    private String anotacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
