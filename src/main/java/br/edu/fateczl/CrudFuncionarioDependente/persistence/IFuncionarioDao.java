package br.edu.fateczl.CrudFuncionarioDependente.persistence;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.CrudFuncionarioDependente.model.Funcionario;

public interface IFuncionarioDao {
	
	public String iudFuncionario (String acao, Funcionario f) throws SQLException, ClassNotFoundException;
	public List<Funcionario> listarFuncionario() throws SQLException, ClassNotFoundException;

}
