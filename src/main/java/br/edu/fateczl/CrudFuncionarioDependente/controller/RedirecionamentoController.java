package br.edu.fateczl.CrudFuncionarioDependente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RedirecionamentoController {

    @RequestMapping("/redirecionar")
    public ModelAndView redirecionarConsulta(int codigoFuncionario) {
        ModelAndView modelAndView = new ModelAndView("redirect:/consulta");
        modelAndView.addObject("codigo", codigoFuncionario); 
        return modelAndView;
    }
}