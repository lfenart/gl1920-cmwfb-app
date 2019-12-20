package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Step;
import fr.uha.ensisa.gl.cmwfb.mantest.StepReport;
import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.TestBook;
import fr.uha.ensisa.gl.cmwfb.mantest.TestReport;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;

@Controller
public class TestReportController {

	@Autowired
	public DaoFactory daoFactory;
	
	@RequestMapping(value = "/createReport")
	public String createReport(@RequestParam(required = true) long testBookId,@RequestParam(required = true) long id) {
		Test test = this.daoFactory.getTestDao().find(id);
		if (test != null) {
			TestReport testReport = this.daoFactory.getTestReportDao().create(test);			
			if(testReport.getNextStep() != null) {
				return "redirect:/makeReport?testBookId="+ testBookId +"&id=" + testReport.getId();	
				}
				else {
					return "redirect:/test?id=" + id + "&testBookId=" + testBookId;
				}
		}
		return "redirect:/test?id=" + id + "&testBookId=" + testBookId;
	}

	@RequestMapping(value = "/makeReport")
	public ModelAndView makeReportTask(@RequestParam(required = true) long testBookId, @RequestParam long id) {
		ModelAndView ret = new ModelAndView("makeReport");
		TestReport testReport = daoFactory.getTestReportDao().find(id);
		Test test = testReport.getTest();
		Step nextStep = testReport.getNextStep();
		ret.addObject("testBookId", testBookId);
		ret.addObject("id", id);
		ret.addObject("test", test);
		ret.addObject("nextStep", nextStep);
		ret.addObject("testReport", testReport);
		return ret;
	}

	@RequestMapping(value = "/addStepReport")
	public String addStepReport(@RequestParam(required = true) long testBookId, @RequestParam(required = true) long id,
			@RequestParam(required = true) String comment, @RequestParam(required = true) boolean success) {
		TestReport testReport = daoFactory.getTestReportDao().find(id);
		testReport.next(success, comment);
		if(testReport.getNextStep() != null) {
			return "redirect:/makeReport?testBookId="+testBookId+"&id=" + testReport.getId();	
		}
		else{
			return "redirect:/viewReport?testBookId="+testBookId+"&id=" + testReport.getId();	
		}
	}

	@RequestMapping(value = "/viewReport")
	public  ModelAndView reportTask(@RequestParam(required = true) long testBookId, @RequestParam long id) {
		TestReport currentReport = daoFactory.getTestReportDao().find(id);
		Test currentTest = currentReport.getTest();
		List<Step> currentSteps = currentTest.getSteps();
		List<StepReport> stepReports = currentReport.getStepReports();
		ModelAndView ret = new ModelAndView("viewReport");
		Calendar c = currentReport.getCalendar();
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy à hh:mm:ss aa");
		String datetime = dateformat.format(c.getTime());
		ret.addObject("dateTime", datetime);
		ret.addObject("test", currentTest);
		ret.addObject("testReport", currentReport);
		ret.addObject("Steps", currentSteps);
		ret.addObject("StepReports", stepReports);
		ret.addObject("testBookId", testBookId );
		return ret;
	}

}