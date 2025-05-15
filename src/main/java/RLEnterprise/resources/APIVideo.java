/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Pichau
 */
@RestController
@RequestMapping("/api")
public class APIVideo {

    @Autowired
    private UserService us;

    @GetMapping("/plano") // Entra no plano api
    public ResponseEntity<Map<String, Integer>> getPlano(HttpSession session) {

        // FAZER OS PLANOS DO USUARIO E TRANSFORMAR O PLANO SIMULADO EM PLANO REAL DO
        // USUARIO, COMEÇAR A TRABALHAR OS PLANOS!!

        int planoSimulado = 3; // Aqui é pra pegar o plano do usuario, tem que fazer ainda
        UserProfileDTO user = (UserProfileDTO) session.getAttribute("user"); // Faz um USERDTO com o usuario da sessao

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Usuário não logado
        }

        return ResponseEntity.ok(Map.of("plano", planoSimulado)); // Retorna o plano que vai ser pego do usuario
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<?> getVideo(@PathVariable int id, HttpSession session) {

        int planoSimulado = 3; // Plano pra ser pego do usuario
        UserProfileDTO user = (UserProfileDTO) session.getAttribute("user"); // DTO transformado

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Retorna 401
        }

        if (id > planoSimulado) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Retorna 403
        }

        String videoPath = "/videos/modulo" + id + ".mp4"; // Caminho do vide

        File videoFile = new File("src/main/resources/static" + videoPath); // Abrindo o arquivo com file

        if (!videoFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vídeo não encontrado.");
        }

        try {
            Resource videoResource = new UrlResource(videoFile.toURI()); // Transforma o file em uma URL acessivel
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // Informa o tipo
                    .body(videoResource); // Envia o video
        }

        catch (MalformedURLException e) { // ERROR HTTP 50
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao carregaar o vídeo.");
        }
    }

}
