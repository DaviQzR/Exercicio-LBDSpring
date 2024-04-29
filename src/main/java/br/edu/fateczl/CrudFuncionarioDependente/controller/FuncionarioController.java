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

import br.edu.fateczl.CrudFuncionarioDependente.model.Funcionario;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.FuncionarioDao;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.GenericDao;

@Controller
public class FuncionarioController {

	@Autowired
	GenericDao gDao;

	@Autowired
	FuncionarioDao fDao;

	@RequestMapping(name = "funcionario", value = "/funcionario", method = RequestMethod.GET)
	public ModelAndView indexGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("cmd");
		String codigo = allRequestParam.get("codigo");

		if (cmd != null) {
			Funcionario f = new Funcionario();
			f.setCodigo(Integer.parseInt(codigo));

			String saida = "";
			String erro = "";
			List<Funcionario> funcionarios = new ArrayList<>();

			try {
				if (cmd.contains("alterar")) {
					f = buscarFuncionario(f);
				} else if (cmd.contains("excluir")) {
					saida = excluirFuncionario(f);
				}

				funcionarios = listarFuncionarios();

			} catch (SQLException | ClassNotFoundException e) {
				erro = e.getMessage();
			} finally {

				model.addAttribute("saida", saida);
				model.addAttribute("erro", erro);
				model.addAttribute("funcionario", f);
				model.addAttribute("funcionarios", funcionarios);

			}
			

		}

		return new ModelAndView("funcionario");

	}

	@RequestMapping(name = "funcionario", value = "/funcionario", method = RequestMethod.POST)
	public ModelAndView indexPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		// Entrada
		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");
		String nome = allRequestParam.get("nome");
		String salario = allRequestParam.get("salario");

		// Saida
		String saida = "";
		String erro = "";
		Funcionario f = new Funcionario();
		List<Funcionario> funcionarios = new ArrayList<>();

		if (!cmd.contains("Listar")) {
			f.setCodigo(Integer.parseInt(codigo));
		}
		if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
			f.setNome(nome);
			f.setSalario(Float.parseFloat(salario));
		}

		try {
			if (cmd.contains("Cadastrar")) {
				saida = cadastrarFuncionario(f);
				f = null;
			}
			if (cmd.contains("Alterar")) {
				saida = alterarFuncionario(f);
				f = null;
			}
			if (cmd.contains("Excluir")) {
				saida = excluirFuncionario(f);
				f = null;
			}
			if (cmd.contains("Buscar")) {
				f = buscarFuncionario(f);
			}
			if (cmd.contains("Listar")) {
				funcionarios = listarFuncionarios();
			}

		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("funcionario", f);
			model.addAttribute("funcionarios", funcionarios);
		}

		return new ModelAndView("funcionario");
	}

	private String cadastrarFuncionario(Funcionario f) throws SQLException, ClassNotFoundException {
		String saida = fDao.iudFuncionario("I", f);
		return saida;

	}

	private String alterarFuncionario(Funcionario f) throws SQLException, ClassNotFoundException {
		String saida = fDao.iudFuncionario("U", f);
		return saida;

	}

	private String excluirFuncionario(Funcionario f) throws SQLException, ClassNotFoundException {
		Float salario = (f.getSalario() != null) ? f.getSalario().floatValue() : 0.0f;
	    f.setSalario(salario);
	    String saida = fDao.iudFuncionario("D", f);
	    return saida;
	}

	private Funcionario buscarFuncionario(Funcionario f) throws SQLException, ClassNotFoundException {
		f = fDao.consultar(f);
		return f;

	}

	private List<Funcionario> listarFuncionarios() throws SQLException, ClassNotFoundException {
		List<Funcionario> professores = fDao.listar();
		return professores;
	}

}