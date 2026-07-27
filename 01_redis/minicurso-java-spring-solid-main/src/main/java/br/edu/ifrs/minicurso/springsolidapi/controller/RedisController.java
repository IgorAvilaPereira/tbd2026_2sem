package br.edu.ifrs.minicurso.springsolidapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisOperations<String, String> operations;

    @GetMapping
    public ResponseEntity<String> save() {
        operations.opsForValue().set("igordsfdsaf", "tesdsafsdafsdate");        
        return ResponseEntity.ok().body("ok");
    }   
}
