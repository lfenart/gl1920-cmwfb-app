package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;

@Controller
public class HomeController {

	@Autowired
	public DaoFactory daoFactory;

	@RequestMapping(value = "/hello")
	public ModelAndView hello(@RequestParam(required = false, defaultValue = "World") String name) {
		ModelAndView ret = new ModelAndView("home");
		// Adds an objet to be used in home.jsp
		ret.addObject("name", name);
		return ret;
	}

	@RequestMapping(value = "/list")
	public ModelAndView list() throws IOException {
		ModelAndView ret = new ModelAndView("list");
		ret.addObject("tests", daoFactory.getTestDao().findAll());
		return ret;
	}

	@RequestMapping(value = "/create")
	public String create(@RequestParam(required = true) String testName) throws IOException {
		Test newTest = new Test();
		long id = daoFactory.getTestDao().count() + 1;
		while (daoFactory.getTestDao().find(id) != null)
			id++;
		newTest.setId(id);
		newTest.setName(testName);
		daoFactory.getTestDao().persist(newTest);
		return "redirect:/list";
	}
}
