package br.edu.fateczl.CrudFuncionarioDependente.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dependente {
	
	int codigo;	
	Funcionario funcionario;		
	String nome;			
	Float salario;	
	@Override
	
	public String toString() {
		return nome;
	} 

}