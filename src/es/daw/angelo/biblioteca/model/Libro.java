package es.daw.angelo.biblioteca.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Libro {
    String isbn;
    String titulo;
    int anio_publicacion;
    int autor_id;
    int categoria_id;
}
