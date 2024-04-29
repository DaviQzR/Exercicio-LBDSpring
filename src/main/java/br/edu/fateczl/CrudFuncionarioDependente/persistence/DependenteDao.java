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
import br.edu.fateczl.CrudFuncionarioDependente.model.Dependente;
import br.edu.fateczl.CrudFuncionarioDependente.model.Funcionario;

@Repository
public class DependenteDao implements ICrud<Dependente>, IDependenteDao {

	private GenericDao gDao;

	public DependenteDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public Dependente consultar(Dependente d) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  d.codigo AS codigoDependente, f.codigo AS codigoFuncionario, ");
		sql.append("f.nome AS nomeFuncionario, d.nome AS nomeDependente, ");
		sql.append("f.salario AS salarioFuncionario, d.salario AS salarioDependente ");
		sql.append("FROM funcionario f INNER JOIN dependente d ON f.codigo = d.funcionario  ");

		sql.append("WHERE d.codigo = ?");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setInt(1, d.getCodigo());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			Funcionario f = new Funcionario();
			f.setCodigo(rs.getInt("codigoFuncionario"));
			f.setNome(rs.getString("nomeFuncionario"));
			f.setSalario(rs.getFloat("salarioFuncionario"));

			d.setCodigo(rs.getInt("codigoDependente"));
			d.setFuncionario(f);
			d.setNome(rs.getString("nomeDependente"));
			d.setSalario(rs.getFloat("salarioFuncionario"));

		}
		rs.close();
		ps.close();
		c.close();

		return d;
	}

	@Override
	public List<Dependente> listar() throws SQLException, ClassNotFoundException {
		List<Dependente> dependentes = new ArrayList<>();
		Connection c = gDao.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM fn_funcionarios_dependentes() ");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Funcionario f = new Funcionario();
			f.setCodigo(rs.getInt("codigoFuncionario"));
			f.setNome(rs.getString("nomeFuncionario"));
			f.setSalario(rs.getFloat("salarioFuncionario"));

			Dependente d = new Dependente();

			d.setCodigo(rs.getInt("codigoDependente"));
			d.setNome(rs.getString("nomeDependente"));
			d.setSalario(rs.getFloat("salarioFuncionario"));
			d.setFuncionario(f);
			dependentes.add(d);
		}
		rs.close();
		ps.close();
		c.close();
		return dependentes;
	}

	@Override
	public String iudDependente(String acao, Dependente d) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "{CALL sp_iud_dependente (?,?,?,?,?,?)}";
		CallableStatement cs = c.prepareCall(sql);
		cs.setString(1, acao);
		cs.setInt(2, d.getCodigo());
		cs.setString(3, d.getNome());
		cs.setFloat(4, d.getSalario());
		cs.setInt(5, d.getFuncionario().getCodigo());
		cs.registerOutParameter(6, Types.VARCHAR);
		cs.execute();
		String saida = cs.getString(6);
		cs.close();
		c.close();

		return saida;
	}

	@Override
	public List<Dependente> listarDependente(int codigoFuncionario) throws SQLException, ClassNotFoundException {
		List<Dependente> dependentes = new ArrayList<>();
		Connection con = gDao.getConnection();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT d.codigoDependente, d.nomeDependente, d.salarioDependente, ");
		sql.append("f.nome AS nomeFuncionario, f.salario as salarioFuncionario ");
		sql.append("FROM fn_consultar_funcionarios_dependentes(?) d ");
		sql.append("INNER JOIN funcionario f ON d.codigoFuncionario = f.codigo");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, codigoFuncionario);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Dependente dependente = new Dependente();
			dependente.setCodigo(rs.getInt("codigoDependente"));
			dependente.setNome(rs.getString("nomeDependente"));
			dependente.setSalario(rs.getFloat("salarioDependente"));

			Funcionario funcionario = new Funcionario();
			funcionario.setNome(rs.getString("nomeFuncionario"));
			funcionario.setSalario(rs.getFloat("salarioFuncionario"));
			dependente.setFuncionario(funcionario);

			dependentes.add(dependente);
		}

		rs.close();
		ps.close();
		con.close();

		return dependentes;
	}

	public Float calcularSomaSalario(int codigo) throws SQLException, ClassNotFoundException {
		Connection con = gDao.getConnection();
		String sql = "{ ? = call fn_soma_salario(?) }";
		CallableStatement cs = con.prepareCall(sql);
		cs.registerOutParameter(1, Types.DECIMAL);
		cs.setInt(2, codigo);
		cs.execute();
		Float totalSalario = cs.getFloat(1);
		cs.close();
		con.close();
		return totalSalario;
	}
}