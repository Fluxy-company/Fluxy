package school.sptech.iefcbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Video_id")
    private Long id;

    private String titulo;

    private String url;

    @ManyToMany
    @JoinTable(
            name = "curso_video",
            joinColumns = @JoinColumn(name = "idVideo"),
            inverseJoinColumns = @JoinColumn(name = "idEvento"))
    private List<Curso> cursos;

}
