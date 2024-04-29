package br.edu.fateczl.CrudFuncionarioDependente.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import br.edu.fateczl.CrudFuncionarioDependente.model.Funcionario;

@Repository
public class FuncionarioDao implements ICrud<Funcionario>, IFuncionarioDao {

	private GenericDao gDao;

	public FuncionarioDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public Funcionario consultar(Funcionario f) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT codigo, nome, salario FROM funcionario WHERE codigo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, f.getCodigo());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			f.setCodigo(rs.getInt("codigo"));
			f.setNome(rs.getString("nome"));
			f.setSalario(rs.getFloat("salario"));
		}
		rs.close();
		ps.close();
		c.close();
		return f;
	}

	@Override
	public List<Funcionario> listar() throws SQLException, ClassNotFoundException {

		List<Funcionario> funcionarios = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT codigo, nome, salario FROM funcionario";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			Funcionario f = new Funcionario();
			f.setCodigo(rs.getInt("codigo"));
			f.setNome(rs.getString("nome"));
			f.setSalario(rs.getFloat("salario"));
			funcionarios.add(f);
		}
		rs.close();
		ps.close();
		c.close();
		return funcionarios;
	}

	@Override
	public String iudFuncionario(String acao, Funcionario f) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "{CALL sp_iud_funcionario (?,?,?,?,?)}";
		CallableStatement cs = c.prepareCall(sql);
		cs.setString(1, acao);
		cs.setInt(2, f.getCodigo());
		cs.setString(3, f.getNome());
	    cs.setDouble(4, f.getSalario());
		cs.registerOutParameter(5, Types.VARCHAR);
		cs.execute();
		String saida = cs.getString(5);
		cs.close();
		c.close();

		return saida;
	}

	@Override
	public List<Funcionario> listarFuncionario() throws SQLException, ClassNotFoundException {
		List<Funcionario> funcionarios = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT codigo, nome, salario FROM fn_funcionarios_dependentes()";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			Funcionario f = new Funcionario();
			f.setCodigo(rs.getInt("codigo"));
			f.setNome(rs.getString("nome"));
			f.setSalario(rs.getFloat("salario"));
			funcionarios.add(f);
		}
		rs.close();
		ps.close();
		c.close();
		return funcionarios;
	}
	
	
	
	


}