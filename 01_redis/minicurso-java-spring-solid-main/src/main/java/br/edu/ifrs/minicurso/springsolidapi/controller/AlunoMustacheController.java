package br.edu.ifrs.minicurso.springsolidapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aluno_mustache")
public class AlunoMustacheController {
    
    @GetMapping("/")
    public ModelAndView index() {
        Map<String, Object> template = new HashMap<String, Object>();
        template.put("mensagem", "deu certo!");
        return new ModelAndView("index", template);
    }

}