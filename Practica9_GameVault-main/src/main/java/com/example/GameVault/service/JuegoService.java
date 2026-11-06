package com.example.GameVault.service;

import com.example.GameVault.model.Juego;
import com.example.GameVault.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";


    public List<Juego> listarTodos(){
        return juegoRepository.findAll();
    }

    public Juego obtenerJuegoPorId(Long id){
        return juegoRepository.findById(id).orElse(null);
    }

    public void eliminarJuego(Long id){
        juegoRepository.deleteById(id);
    }

    public void guardarJuego(Juego juego, MultipartFile portada){
        String nombreArchivo = "default.png";

        // Verificamos si el archivo no está vacío
        if (portada != null && !portada.isEmpty()) {
            try {

                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                nombreArchivo = UUID.randomUUID().toString() + "_" + portada.getOriginalFilename();
                Path filePath = uploadPath.resolve(nombreArchivo);


                Files.copy(portada.getInputStream(), filePath);

                System.out.println("Archivo guardado en: " + filePath.toAbsolutePath());

                juego.setPortadaUrl(nombreArchivo);

            } catch (IOException e) {
                e.printStackTrace();
                // En un proyecto real, manejaríamos esta excepción apropiadamente (ej. redirigir con error)
            }
        }else if(juego.getId() == null || juego.getPortadaUrl() == null){
            juego.setPortadaUrl(nombreArchivo);
        }


        juegoRepository.save(juego);
    }

    public List<Juego> buscarPorTitulo(String palabra){
        if(palabra== null || palabra.trim().isEmpty()){
            return listarTodos();
        }
        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }


    public List<Juego> obtenerJuegosPorUsuario(Long usuarioID){
        return juegoRepository.findByUsuarioID(usuarioID);
    }
}
