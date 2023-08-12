
package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.imagen;
import org.springframework.data.jpa.repository.JpaRepository;


public interface imagenRepositorio extends JpaRepository<imagen, String> {
    
}
