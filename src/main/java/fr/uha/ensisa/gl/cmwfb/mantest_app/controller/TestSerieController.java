package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.TestBook;
import fr.uha.ensisa.gl.cmwfb.mantest.TestSerie;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;

@Controller
public class TestSerieController {

	@Autowired
	public DaoFactory daoFactory;

	@RequestMapping(value = "/creationSerieForm")
	public ModelAndView creationSerieForm(@RequestParam(required = true) Long testBookId) {
		ModelAndView ret = new ModelAndView("createTestSerie");
		ret.addObject("testBookId", testBookId);
		return ret;
	}

	@RequestMapping(value = "/createSerie")
	public String createSerie(@RequestParam(required = true) Long testBookId,
			@RequestParam(required = true) String name) {
		TestSerie testSerie = this.daoFactory.getTestSerieDao().create(name);
		this.daoFactory.getTestSerieDao().persist(testSerie);
		TestBook testBook = daoFactory.getTestBookDao().find(testBookId);
		testBook.addTestSerie(testSerie);
		return "redirect:/viewSerie?testBookId=" + testBookId + "&id=" + testSerie.getId();
	}

	@RequestMapping(value = "/addTest")
	public String addTest(@RequestParam(required = true) Long testBookId, @RequestParam(required = true) long serieId,
			@RequestParam(required = true) long testId) {
		Test test = this.daoFactory.getTestDao().find(testId);
		TestSerie testSerie = this.daoFactory.getTestSerieDao().find(serieId);
		testSerie.add(test);
		return "redirect:/viewSerie?testBookId=" + testBookId + "&id=" + serieId;
	}

	@RequestMapping(value = "/removeTest")
	public String removeTest(@RequestParam(required = true) Long testBookId,
			@RequestParam(required = true) long serieId, @RequestParam(required = true) long testId) {
		Test test = this.daoFactory.getTestDao().find(testId);
		TestSerie testSerie = this.daoFactory.getTestSerieDao().find(serieId);
		testSerie.remove(test);
		return "redirect:/viewSerie?testBookId=" + testBookId + "&id=" + serieId;
	}

	@RequestMapping(value = "/addTestSerie")
	public String addTestSerie(@RequestParam(required = true) Long testBookId,
			@RequestParam(required = true) long serieId, @RequestParam(required = true) long testSerieId) {
		TestSerie test = this.daoFactory.getTestSerieDao().find(testSerieId);
		TestSerie testSerie = this.daoFactory.getTestSerieDao().find(serieId);
		testSerie.add(test);
		return "redirect:/viewSerie?testBookId=" + testBookId + "&id=" + serieId;
	}

	@RequestMapping(value = "/removeTestSerie")
	public String removeTestSerie(@RequestParam(required = true) Long testBookId,
			@RequestParam(required = true) long serieId, @RequestParam(required = true) long testSerieId) {
		TestSerie test = this.daoFactory.getTestSerieDao().find(testSerieId);
		TestSerie testSerie = this.daoFactory.getTestSerieDao().find(serieId);
		testSerie.removeSerie(test);
		return "redirect:/viewSerie?testBookId=" + testBookId + "&id=" + serieId;
	}

	@RequestMapping(value = "/viewSerie")
	public ModelAndView reportSerie(@RequestParam(required = true) Long testBookId, @RequestParam long id) {
		TestSerie currentSerie = daoFactory.getTestSerieDao().find(id);
		Test[] current = daoFactory.getTestSerieDao().find(id).getTests();
		ModelAndView ret = new ModelAndView("viewSerie");
		ret.addObject("listTests", daoFactory.getTestBookDao().find(testBookId).getTestList());
		ret.addObject("Lists", daoFactory.getTestBookDao().find(testBookId).getTestSerieList());
		ret.addObject("tests", current);
		ret.addObject("id", id);
		ret.addObject("name", currentSerie.getName());
		ret.addObject("series", currentSerie.getTestSeries());
		ret.addObject("testBookId", testBookId);
		return ret;
	}

	@RequestMapping(value = "/deleteSerie")
	public String deleteSerie(@RequestParam(required = true) Long testBookId, @RequestParam(required = true) long id) {
		TestSerie test = daoFactory.getTestSerieDao().find(id);
		if (test != null) {
			daoFactory.getTestSerieDao().deleteSerie(test);
			daoFactory.getTestBookDao().find(testBookId).remove(test);
		}
		return "redirect:/list?testBookId=" + testBookId;
	}
}
