
package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.imagen;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.imagenRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class imagenServicio {
    @Autowired
    private imagenRepositorio imagenrepositorio;
    
    public imagen guardar(MultipartFile archivo)throws MiException{
    
        if(archivo != null){
            try{
            
                imagen imagen = new imagen();
                
                imagen.setMime(archivo.getContentType());
                
                imagen.setNombre(archivo.getName());
                
                imagen.setContenido(archivo.getBytes());
                
                return imagenrepositorio.save(imagen);
            } catch(Exception e){
            
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    public imagen actualizar(MultipartFile archivo, String idImagen)throws MiException{
     
          if(archivo != null){
            try{
            
                imagen imagen = new imagen();
                
                if(idImagen != null){
                
                  Optional<imagen> respuesta = imagenrepositorio.findById(idImagen);
                  
                  if(respuesta.isPresent()){
                  
                      imagen = respuesta.get();
                  }
                } 
               
                imagen.setMime(archivo.getContentType());
                
                imagen.setNombre(archivo.getName());
                
                imagen.setContenido(archivo.getBytes());
                
                return imagenrepositorio.save(imagen);
            } catch(Exception e){
            
                System.err.println(e.getMessage());
            }
        }
        return null;
        
    }
    
     
}
