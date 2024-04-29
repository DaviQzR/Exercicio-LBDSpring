package br.edu.fateczl.CrudFuncionarioDependente.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import br.edu.fateczl.CrudFuncionarioDependente.model.Dependente;
import br.edu.fateczl.CrudFuncionarioDependente.model.Funcionario;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.DependenteDao;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.FuncionarioDao;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.GenericDao;

@Controller
public class DependenteController {
	
	@Autowired
	GenericDao gDao;
	
	@Autowired
	FuncionarioDao fDao;
	
	@Autowired
	DependenteDao dDao;

	@RequestMapping(name = "dependente", value = "/dependente", method = RequestMethod.GET)
	public ModelAndView dependenteGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("cmd");
		String codigo = allRequestParam.get("codigo");

		if (cmd != null) {
			Dependente d = new Dependente();
			d.setCodigo(Integer.parseInt(codigo));

			String saida = "";
			String erro = "";
			List<Dependente> dependentes = new ArrayList<>();
			List<Funcionario> funcionarios = new ArrayList<>();

			try {
				if (cmd.contains("alterar")) {
					d = buscarDependente(d);
				} else if (cmd.contains("excluir")) {
					saida = excluirDependente(d);
				}

				dependentes = listarDependentes();
				funcionarios= listarFuncionarios();

			} catch (SQLException | ClassNotFoundException e) {
				erro = e.getMessage();
			} finally {

				model.addAttribute("saida", saida);
				model.addAttribute("erro", erro);
				model.addAttribute("dependente", d);
				model.addAttribute("dependentes", dependentes);
				model.addAttribute("funcionarios", funcionarios);

			}

		}

		return new ModelAndView("dependente");

	}


	@RequestMapping(name = "dependente", value = "/dependente", method = RequestMethod.POST)
	public ModelAndView dependentePost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		
		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");		
		String nome = allRequestParam.get("nome");
		String salario = allRequestParam.get("salario");
		String funcionario = allRequestParam.get("funcionario");

		// saida
		String saida = "";
		String erro = "";
		Dependente d = new Dependente();
		
		List<Dependente> dependentes = new ArrayList<>();
		List<Funcionario> funcionarios = new ArrayList<>();
		
		if (!cmd.contains("Listar")) {
			d.setCodigo(Integer.parseInt(codigo));
		}
		
		try {
			
			funcionarios = listarFuncionarios();
			
			if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
				d.setNome(nome);
				d.setSalario(Float.parseFloat(salario));
				
				Funcionario p = new Funcionario();
				p.setCodigo(Integer.parseInt(funcionario));
				p = buscarFuncionario(p);
				d.setFuncionario(p);

			}
			if (cmd.contains("Cadastrar")) {
				saida = cadastrarDependente(d);
				d = null;
			}
			if (cmd.contains("Alterar")) {
				saida = alterarDependente(d);
				d = null;
			}
			if (cmd.contains("Excluir")) {
				d = buscarDependente(d);
				saida = excluirDependente(d);
				d = null;
			}
			if (cmd.contains("Buscar")) {
				d = buscarDependente(d);
			}
			if (cmd.contains("Listar")) {
				dependentes = listarDependentes();
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();

		} finally {
			
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("dependente", d);
			model.addAttribute("dependentes", dependentes);
			model.addAttribute("funcionarios", funcionarios);
			
		}
		
	
		return new ModelAndView("dependente");

		
	}
	
	private String cadastrarDependente(Dependente p) throws SQLException, ClassNotFoundException {
		String saida = dDao.iudDependente("I", p);
		return saida;

	}

	private String alterarDependente(Dependente d) throws SQLException, ClassNotFoundException {
		String saida = dDao.iudDependente("U", d);
		return saida;

	}

	private String excluirDependente(Dependente d) throws SQLException, ClassNotFoundException {
		
		String saida = dDao.iudDependente("D", d);
		return saida;

	}

	private Dependente buscarDependente(Dependente d) throws SQLException, ClassNotFoundException {
		d = dDao.consultar(d);
		return d;

	}

	private List<Dependente> listarDependentes() throws SQLException, ClassNotFoundException {	
		List< Dependente> dependentes = dDao.listar();
		return dependentes;
	}

	private Funcionario buscarFuncionario(Funcionario f) throws SQLException, ClassNotFoundException {
		f = fDao.consultar(f);
		return f;

	}

	private List<Funcionario> listarFuncionarios() throws SQLException, ClassNotFoundException {
		List<Funcionario> funcionarios = fDao.listar();
		return funcionarios;
	}

}