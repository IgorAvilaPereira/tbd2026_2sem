package br.edu.ifrs.minicurso.springsolidapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.minicurso.springsolidapi.infrastructure.RedisCacheConfig;
import br.edu.ifrs.minicurso.springsolidapi.model.Usuario;
import br.edu.ifrs.minicurso.springsolidapi.service.UsuarioRedisService;
import jakarta.persistence.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/usuarios_cache")
public class UsuarioControllerCache {

    private final RedisCacheConfig redisCacheConfig;

    public UsuarioControllerCache(RedisCacheConfig redisCacheConfig) {
        this.redisCacheConfig = redisCacheConfig;
    }

    @GetMapping    
    @org.springframework.cache.annotation.Cacheable("oi")
    public Usuario buscarUsuario(Long id) {
        // consulta ao banco
        Usuario teste = new Usuario();
        teste.setId("3");
        teste.setNome("oi");
        teste.setEmail("ola");
        return teste;
    }


}
