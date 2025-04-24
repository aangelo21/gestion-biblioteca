package es.daw.angelo.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Autor {
    int id_autor;
    String nombre;
    String nacionalidad;
}
