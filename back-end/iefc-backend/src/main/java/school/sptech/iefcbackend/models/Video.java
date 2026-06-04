package school.sptech.iefcbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVideo")
    private Long id;

    private String titulo;

    private String url;

    @Column(name = "video_id", length = 20)
    private String videoId;

    @Column(name = "duracao", length = 10)
    private String duracao;

    @Column(name = "modulo", length = 100)
    private String modulo;

    @Column(name = "ordem")
    private Integer ordem;

    @ManyToOne
    @JoinColumn(name = "idCurso")
    @JsonBackReference
    private Curso curso;

}
