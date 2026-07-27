package br.edu.ifrs.minicurso.springsolidapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifrs.minicurso.springsolidapi.model.Usuario;
import br.edu.ifrs.minicurso.springsolidapi.service.UsuarioRedisService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private final UsuarioRedisService usuarioService;

    public UsuarioController(UsuarioRedisService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /*
     * {
     * "id": "1",
     * "nome": "Maria Silva",
     * "email": "maria.silva@email.com"
     * }
     */

    @PostMapping
    public void criar(@RequestBody Usuario usuario) {
        usuarioService.salvar(usuario);
    }

    // http://localhost:8080/usuarios/usuario:1
    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable String id) {    
        return (Usuario) usuarioService.buscar(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable String id) {
        usuarioService.deletar(id);
    }

    @GetMapping("/")
    public List<Usuario> listar() {
        // System.out.println(usuarioService.listar());
        return usuarioService.listar();
    }

    @GetMapping("/cache")  
    public Usuario salvarNoCache() {
        Usuario teste = new Usuario();
        teste.setId("3");
        teste.setNome("oi");
        teste.setEmail("ola");        
        usuarioService.salvarNoCache(1, teste);
        return teste;
    }
    
}
