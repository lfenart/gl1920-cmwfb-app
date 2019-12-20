package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Step;
import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.TestBook;
import fr.uha.ensisa.gl.cmwfb.mantest.TestReport;
import fr.uha.ensisa.gl.cmwfb.mantest.TestSerie;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;

@Controller
public class TestController {

	@Autowired
	public DaoFactory daoFactory;

	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam(required = true) long testBookId) throws IOException {
		ModelAndView ret = new ModelAndView("list");
		ret.addObject("testBook", daoFactory.getTestBookDao().find(testBookId));
		ret.addObject("tests", daoFactory.getTestDao().findAll());
		ret.addObject("testReports", daoFactory.getTestReportDao().findAll());
		return ret;
	}

	@RequestMapping(value = "/create")
	public String create(@RequestParam(required = true) long testBookId, @RequestParam(required = true) String testName) throws IOException {
		Test newTest = new Test();
		long id = daoFactory.getTestDao().count() + 1;
		while (daoFactory.getTestDao().find(id) != null)
			id++;
		newTest.setId(id);
		newTest.setName(testName);
		daoFactory.getTestDao().persist(newTest);
		TestBook testBook = daoFactory.getTestBookDao().find(testBookId);
		testBook.addTest(newTest);
		return "redirect:/list?testBookId="+ testBookId;
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

	@RequestMapping(value = "/addStep")
	public String addStep(@RequestParam(required = true) long id, @RequestParam(required = true) String stepName,
			@RequestParam String text) {
		Test test = daoFactory.getTestDao().find(id);
		Step step = new Step();
		step.setName(stepName);
		step.setText(text);
		test.addStep(step);
		return "redirect:/modify?id=" + id;
	}

	@RequestMapping(value = "/testmodifiedname")
	public String TestModifiedName(@RequestParam(required = true) long id,
			@RequestParam(required = true) String newTestName) throws IOException {
		Test test = daoFactory.getTestDao().find(id);
		if (test == null) {
			testNotFound();
		} else {
			test.setName(newTestName);
		}
		return "redirect:/modify?id=" + id;
	}

	@RequestMapping(value = "/testmodifiedstep")
	public String TestModifiedStep(@RequestParam(required = true) long testId,
			@RequestParam(required = true) int stepId, @RequestParam(required = true) String newStepName,
			@RequestParam String text) throws IOException {
		Test test = daoFactory.getTestDao().find(testId);
		if (test != null) {
			Step step = test.getStep(stepId);
			if (step != null) {
				step.setName(newStepName);
				step.setText(text);
			}
		}

		return "redirect:/modify?id=" + testId;
	}

	@RequestMapping(value = "/testDeleteStep")
	public String testDeleteStep(@RequestParam(required = true) long testId,
			@RequestParam(required = true) int stepId) {
		Test test = daoFactory.getTestDao().find(testId);
		if (test != null) {
			Step step = test.getStep(stepId);
			if (step != null) {
				test.removeStep(stepId);
			}
		}

		return "redirect:/modify?id=" + testId;
	}

	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(required = true) long testBookId, @RequestParam(required = true) long id) {
		TestBook testbook = daoFactory.getTestBookDao().find(testBookId);
		Test test = daoFactory.getTestDao().find(id);
		if (test != null) {
			daoFactory.getTestDao().remove(test);
			testbook.remove(test);
			for (TestSerie serie : daoFactory.getTestSerieDao().findAll()) {
				serie.remove(test);
			}
			TestReport testReport = daoFactory.getTestReportDao().find(id);
			if (testReport != null)
				daoFactory.getTestReportDao().remove(testReport);
		}
		return "redirect:/list?testBookId="+ testBookId;
	}

	@RequestMapping(value = "/test")
	public ModelAndView test(@RequestParam(required = true) long testBookId,@RequestParam(required = true) long id) {
		ModelAndView ret = new ModelAndView("test");
		TestReport currentReport = daoFactory.getTestReportDao().find(id);
		ret.addObject("testReport", currentReport);
		TestBook testbook = daoFactory.getTestBookDao().find(testBookId);
		Test test = daoFactory.getTestDao().find(id);
		if (test == null) {
			return testNotFound();
		} else {
			ret.addObject("test", test);
			ret.addObject("testBook", testbook);
			return ret;
		}
	}

	public ModelAndView testNotFound() {
		return new ModelAndView("notest");
	}
	
}
