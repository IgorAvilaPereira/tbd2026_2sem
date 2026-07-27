package br.edu.ifrs.minicurso.springsolidapi.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String email;

    // getters e setters
}
