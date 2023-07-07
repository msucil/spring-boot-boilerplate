package design.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String isbn;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

    @NotNull
    private LocalDate publishedDate;
}
