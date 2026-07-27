package br.edu.ifrs.minicurso.springsolidapi.controller.mustache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifrs.minicurso.springsolidapi.model.Aluno;
import br.edu.ifrs.minicurso.springsolidapi.repository.AlunoRepository;

@Controller
@RequestMapping("/aluno_mustache")
public class AlunoControllerMustache {
    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping("/tela_adicionar")
    public ModelAndView tela_adicionar() {
        return new ModelAndView("tela_adicionar");
    }

    @GetMapping
    public ModelAndView getAll() {
        List<Aluno> alunos = alunoRepository.findAll();
        Map<String, Object> map = new HashMap();
        map.put("vetAluno", alunos);
        return new ModelAndView("index", map);
    }

    @PostMapping("/adicionar")
    public ModelAndView adicionar(@RequestParam String nome, @RequestParam String sobrenome) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setSobrenome(sobrenome);
        this.alunoRepository.save(aluno);
        return new ModelAndView("redirect:/aluno_mustache");

        
    }


    @GetMapping("/excluir/{id}")
    public ModelAndView delete(@PathVariable int id) {
        alunoRepository.deleteById(Integer.valueOf(id));     
        return new ModelAndView("redirect:/aluno_mustache");        
    }

    @GetMapping("/tela_editar/{id}")
    public ModelAndView tela_editar(@PathVariable int id) {
        Optional<Aluno> optionalAluno = alunoRepository.findById(Integer.valueOf(id));     
        Map<String, Object> map = new HashMap();
        map.put("aluno", optionalAluno.get());
        return new ModelAndView("tela_editar", map);      
    }

    @PostMapping("/editar")
    public ModelAndView editar(@RequestParam String nome, @RequestParam String sobrenome, @RequestParam int id) {
        Aluno aluno = alunoRepository.findById(Integer.valueOf(id)).get();     
        aluno.setNome(nome);
        aluno.setSobrenome(sobrenome);
        this.alunoRepository.save(aluno);
        return new ModelAndView("redirect:/aluno_mustache");

        
    }
}
