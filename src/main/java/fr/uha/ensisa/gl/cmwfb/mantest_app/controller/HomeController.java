package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Step;
import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.TestBook;
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


	@RequestMapping(value = "/books")
	public ModelAndView books() {
		ModelAndView ret = new ModelAndView("books");
		// Adds an objet to be used in home.jsp
		ret.addObject("testBooks", daoFactory.getTestBookDao().findAll());
		return ret;
	}
	
	@RequestMapping(value = "/list")
	public ModelAndView list() throws IOException {
		ModelAndView ret = new ModelAndView("list");
		ret.addObject("tests", daoFactory.getTestDao().findAll());
		return ret;
	}

	@RequestMapping(value = "/createBook")
	public String createBook(@RequestParam(required = true) String testBookName) throws IOException {
		TestBook newTestBook = new TestBook();
		long id = daoFactory.getTestBookDao().count() + 1;
		while (daoFactory.getTestBookDao().find(id) != null)
			id++;
		newTestBook.setId(id);
		newTestBook.setName(testBookName);
		daoFactory.getTestBookDao().persist(newTestBook);
		return "redirect:/books";
	}
	
	@RequestMapping(value = "/create")
	public String create(@RequestParam(required = false) Long testBookId, @RequestParam(required = true) String testName) throws IOException {
		Test newTest = new Test();
		long id = daoFactory.getTestDao().count() + 1;
		while (daoFactory.getTestDao().find(id) != null)
			id++;
		newTest.setId(id);
		newTest.setName(testName);
		daoFactory.getTestDao().persist(newTest);
		if (testBookId != null) {
			TestBook testBook = daoFactory.getTestBookDao().find(testBookId);
			testBook.add(newTest);
			return "redirect:/book?testBookId="+testBookId;
		} else {
			return "redirect:/list";
		}
	}
	
	@RequestMapping(value = "/book")
	public ModelAndView book(@RequestParam(required = true) long testBookId) throws IOException {
		ModelAndView ret = new ModelAndView("book");
		ret.addObject("testBook", daoFactory.getTestBookDao().find(testBookId));
		ret.addObject("tests", daoFactory.getTestBookDao().find(testBookId).getTests());
		ret.addObject("state", daoFactory.getTestBookDao().find(testBookId).getState());

		return ret;
	}
	
	
	@RequestMapping(value = "/modify")
	public ModelAndView modify(@RequestParam(required = true) long id) throws IOException {
		ModelAndView ret = new ModelAndView("modify");
		Test test = daoFactory.getTestDao().find(id);
		if (test == null) {
			return testNotFound();
		} else {
			ret.addObject("test", test);
			return ret;
		}
	}
	
	@RequestMapping(value = "/testmodifiedname")
	public String TestModifiedName(@RequestParam(required = true) long id, @RequestParam(required = true) String newTestName) throws IOException {
		Test test = daoFactory.getTestDao().find(id);
		if (test==null) {
			testNotFound();
		}
		else {
			daoFactory.getTestDao().modifyName(id, newTestName);
		}
		return "redirect:/test?id="+id;
	}
	
	@RequestMapping(value = "/testmodifiedstep")
	public String TestModifiedStep(@RequestParam(required = true) long testId, @RequestParam(required = true) int stepId, @RequestParam(required = true) String newStep) throws IOException {
		Test test = daoFactory.getTestDao().find(testId);
		Step step = test.getStep(stepId);
		if (test==null || step==null) {
			testNotFound();
		}
		else {
			daoFactory.getTestDao().modifyStep(testId, stepId, newStep);
		}
		
		return "redirect:/test?id="+testId;
	}
	
	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(required = true) long id) {
		Test test = daoFactory.getTestDao().find(id);
		if (test != null) {
			daoFactory.getTestDao().remove(test);
		}
		return "redirect:/list";
	}
	
	@RequestMapping(value = "/test")
	public ModelAndView test(@RequestParam(required = false) Long testBookId, @RequestParam(required = true) long testId) {
		ModelAndView ret = new ModelAndView("test");
		Test test = daoFactory.getTestDao().find(testId);
		if (test == null) {
			return testNotFound();
		} else {
			ret.addObject("test", test);
			return ret;
		}
	}

	private ModelAndView testNotFound() {
		return new ModelAndView("notest");
	}
	
	@RequestMapping(value = "/addStep")
	private String addStep(@RequestParam(required = true) long id, @RequestParam(required = true) String stepText) {
		Test test = daoFactory.getTestDao().find(id);
		Step step = new Step();
		step.setText(stepText);
		test.addStep(step);
		return "redirect:/test?id=" + id;
	}

}
