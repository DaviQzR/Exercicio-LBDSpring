package br.edu.fateczl.CrudFuncionarioDependente.persistence;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.CrudFuncionarioDependente.model.Dependente;


public interface IDependenteDao {
	
	public String iudDependente (String acao, Dependente d) throws SQLException, ClassNotFoundException;
	public List<Dependente> listarDependente(int codigo) throws SQLException, ClassNotFoundException;

}