package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Step;
import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.TestReport;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;

@Controller
public class TestController {

	@Autowired
	public DaoFactory daoFactory;

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
	
	
	@RequestMapping(value = "/modify")
	public ModelAndView modify(@RequestParam(required = true) long id) throws IOException {
		ModelAndView ret = new ModelAndView("modify");
		Test test = daoFactory.getTestDao().find(id);
		if (test == null) {
			return testNotFound();
		} else {
			TestReport testReport = daoFactory.getTestReportDao().find(test.getId());
			ret.addObject("test", test);
			ret.addObject("testReport",testReport);
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
			test.setName(newTestName);
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
			step.setText(newStep);
		}
		
		return "redirect:/test?id="+testId;
	}
	
	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(required = true) long id) {
		Test test = daoFactory.getTestDao().find(id);
		if (test != null) {
			daoFactory.getTestDao().remove(test);
			TestReport testReport= daoFactory.getTestReportDao().find(id);
			if (testReport!=null)
				daoFactory.getTestReportDao().remove(testReport);
		}
		return "redirect:/list";
	}
	
	@RequestMapping(value = "/test")
	public ModelAndView test(@RequestParam(required = true) long id) {
		ModelAndView ret = new ModelAndView("test");
		TestReport currentReport = daoFactory.getTestReportDao().find(id);
		ret.addObject("testReport", currentReport);		
		Test test = daoFactory.getTestDao().find(id);
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

}
