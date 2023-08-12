
package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.imagen;
import com.egg.biblioteca.entidades.usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService{
    
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    
    @Autowired
    private imagenServicio imagenservicio;
            
    @Transactional
    public void registrear(MultipartFile archivo, String nombre, String email, String password, String password2) throws MiException{
        
        
        validar(nombre, email, password, password2);
        
        usuario usuario = new usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        
        usuario.setRol(Rol.USER);
        
        imagen imagen = imagenservicio.guardar(archivo);
        
        usuario.setImagen(imagen);
        
        usuarioRepositorio.save(usuario);
        
    }
    
      @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Optional<usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);

            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.USER);
            
            String idImagen = null;
            
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            
            imagen imagen = imagenservicio.actualizar(archivo, idImagen);
            
            usuario.setImagen(imagen);
            
            usuarioRepositorio.save(usuario);
        }

    }
    
    public usuario getOne(String id){
        return usuarioRepositorio.getOne(id);
    }
        @Transactional(readOnly=true)
    public List<usuario> listarUsuarios() {

        List<usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }
    
        @Transactional
    public void cambiarRol(String id){
        Optional<usuario> respuesta = usuarioRepositorio.findById(id);
    	
    	if(respuesta.isPresent()) {
    		
    		usuario usuario = respuesta.get();
    		
    		if(usuario.getRol().equals(Rol.USER)) {
    			
    		usuario.setRol(Rol.ADMIN);
    		
    		}else if(usuario.getRol().equals(Rol.ADMIN)) {
    			usuario.setRol(Rol.USER);
    		}
    	}
    }
    
    private void validar(String nombre, String email, String password, String password2)throws MiException{
    
        if(nombre.isEmpty() || nombre == null){
        
            throw new MiException("el nombre no puede ser nulo o estar vacio");
        }
        if(email.isEmpty()|| email == null){
        
            throw new MiException("El email no puede ser nulo o estar vacio");
        }
        if(password.isEmpty() || password == null || password.length()<5){
        
            throw new MiException("El password no puede ser nulo o estar vacio o tener mas de 5 caracteres");
        }
        if(!password.equals(password2)){
        
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        
        if(usuario != null){
            
            
            List<GrantedAuthority> permisos = new ArrayList();
            
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); 
            
            permisos.add(p);
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
            
            session.setAttribute("usuariosession", usuario);
            
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }else{
        return null;
        
        }
           
        }
    }

