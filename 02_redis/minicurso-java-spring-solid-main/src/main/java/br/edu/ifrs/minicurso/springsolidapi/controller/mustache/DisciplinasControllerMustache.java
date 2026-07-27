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

import br.edu.ifrs.minicurso.springsolidapi.model.Disciplina;
import br.edu.ifrs.minicurso.springsolidapi.repository.DisciplinaRepository;

@Controller
@RequestMapping("/disciplina_mustache")
public class DisciplinasControllerMustache {
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @GetMapping("/tela_adicionar")
    public ModelAndView tela_adicionar() {
        return new ModelAndView("tela_adicionar_disciplina");
    }

    @GetMapping
    public ModelAndView getAll() {
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        Map<String, Object> map = new HashMap();
        map.put("vetDisciplina", disciplinas);
        return new ModelAndView("index_disciplina", map);
    }

    @PostMapping("/adicionar")
    public ModelAndView adicionar(@RequestParam String nome, @RequestParam String sobrenome) {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(nome);
        this.disciplinaRepository.save(disciplina);
        return new ModelAndView("redirect:/disciplina_mustache");

        
    }


    @GetMapping("/excluir/{id}")
    public ModelAndView delete(@PathVariable int id) {
        disciplinaRepository.deleteById(Integer.valueOf(id));     
        return new ModelAndView("redirect:/disciplina_mustache");        
    }

    @GetMapping("/tela_editar/{id}")
    public ModelAndView tela_editar(@PathVariable int id) {
        Optional<Disciplina> optionalAluno = disciplinaRepository.findById(Integer.valueOf(id));     
        Map<String, Object> map = new HashMap();
        map.put("disciplina", optionalAluno.get());
        return new ModelAndView("tela_editar_disciplina", map);      
    }

    @PostMapping("/editar")
    public ModelAndView editar(@RequestParam String nome, @RequestParam String sobrenome, @RequestParam int id) {
        Disciplina disciplina = disciplinaRepository.findById(Integer.valueOf(id)).get();     
        disciplina.setNome(nome);
        this.disciplinaRepository.save(disciplina);
        return new ModelAndView("redirect:/disciplina_mustache");

        
    }
}

