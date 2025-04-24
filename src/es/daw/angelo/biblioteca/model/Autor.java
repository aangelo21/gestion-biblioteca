package es.daw.angelo.biblioteca.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Autor {
    int id_autor;
    String nombre;
    String nacionalidad;
}
