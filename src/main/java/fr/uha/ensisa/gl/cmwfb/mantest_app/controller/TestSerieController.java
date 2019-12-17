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
    @RequestMapping(value = "/addTestSerie")
    public String addTestSerie(@RequestParam(required = true) long serieId, @RequestParam(required = true) long testSerieId){
    		TestSerie test = this.daoFactory.getTestSerieDao().find(testSerieId);
            TestSerie testSerie = this.daoFactory.getTestSerieDao().find(serieId);
            testSerie.add(test);
            this.daoFactory.getTestSerieDao().persist(testSerie);
            return "redirect:/viewSerie?id=" + serieId;
    }
    
	@RequestMapping(value = "/viewSerie")
	public  ModelAndView reportSerie(@RequestParam long id) {
		TestSerie currentSerie = daoFactory.getTestSerieDao().find(id);
		Test[] current = daoFactory.getTestSerieDao().find(id).getTests();
		ModelAndView ret = new ModelAndView("viewSerie");
		ret = new ModelAndView("viewSerie");
		ret.addObject("tests", current);
		ret.addObject("id",id);
		ret.addObject("name", currentSerie.getName());
		ret.addObject("serie", currentSerie.getTestSeries());
		return ret;
	}
	
	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(required = true) long id) {
		TestSerie test = daoFactory.getTestSerieDao().find(id);
		if (test != null) {
			daoFactory.getTestSerieDao().delete(test);
		}
		return "redirect:/viewSerie";
	}
}
