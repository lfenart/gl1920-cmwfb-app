package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.TestSerie;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;
@Controller
public class TestSerieController {

	@Autowired
    public DaoFactory daoFactory;
    
    @RequestMapping(value = "/creationSerieForm")
    public ModelAndView creationSerieForm() {
            ModelAndView ret = new ModelAndView("createTestSerie");
            return ret;
    }
    
    @RequestMapping(value = "/createSerie")
    public String createSerie(@RequestParam(required = true) String name) {
            TestSerie testSerie = this.daoFactory.getTestSerieDao().create(name);
            this.daoFactory.getTestSerieDao().persist(testSerie);
            return "redirect:/viewSerie?id=" + testSerie.getId();
    }
    @RequestMapping(value = "/addTest")
    public String addTest(@RequestParam(required = true) long serieId, @RequestParam(required = true) long testId){
    		Test test = this.daoFactory.getTestDao().find(testId);
            TestSerie testSerie = this.daoFactory.getTestSerieDao().find(serieId);
            testSerie.add(test);
            this.daoFactory.getTestSerieDao().persist(testSerie);
            return "redirect:/viewSerie?id=" + serieId;
    }
}
