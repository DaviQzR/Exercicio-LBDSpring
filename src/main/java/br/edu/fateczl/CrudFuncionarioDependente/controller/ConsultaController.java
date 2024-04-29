package br.edu.fateczl.CrudFuncionarioDependente.controller;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fateczl.CrudFuncionarioDependente.model.Dependente;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.DependenteDao;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.FuncionarioDao;
import br.edu.fateczl.CrudFuncionarioDependente.persistence.GenericDao;

@Controller
public class ConsultaController {

    @Autowired
    GenericDao gDao;

    @Autowired
    FuncionarioDao fDao;

    @Autowired
    DependenteDao dDao;

    @RequestMapping(name = "consulta", value = "/consulta", method = RequestMethod.GET)
    public ModelAndView consultaGet(@RequestParam(value = "codigo", required = false) Integer codigoFuncionario, ModelMap model) {
        if (codigoFuncionario != null) {
            return realizarConsulta(codigoFuncionario, model);
        } else {
            return new ModelAndView("consulta");
        }
    }

    @RequestMapping(name = "consulta", value = "/consulta", method = RequestMethod.POST)
    public ModelAndView dependentePost(@RequestParam("codigo") int codigoFuncionario, ModelMap model) {
        return realizarConsulta(codigoFuncionario, model);
    }

    private ModelAndView realizarConsulta(int codigoFuncionario, ModelMap model) {
        String erro = "";
        List<Dependente> dependentes = null;
        Float somaSalario = null;
        
        try {
            dependentes = listarDependentes(codigoFuncionario);
            somaSalario = calcularSomaSalario(codigoFuncionario);
        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        }
        
        model.addAttribute("erro", erro);
        model.addAttribute("dependentes", dependentes);
        model.addAttribute("somaSalario", somaSalario); 
        
        return new ModelAndView("consulta", model);
    }

    private List<Dependente> listarDependentes(int codigoFuncionario) throws SQLException, ClassNotFoundException {
        return dDao.listarDependente(codigoFuncionario);
    }
    
    private Float calcularSomaSalario(int codigoFuncionario) throws SQLException, ClassNotFoundException {
        return dDao.calcularSomaSalario(codigoFuncionario);
    }
}
