package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.util.List;

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
public class TestReportController {

	@Autowired
	public DaoFactory daoFactory;
	

	@RequestMapping(value = "/createReport")
	public String createReport(@RequestParam(required = true) long id) {
		Test test = this.daoFactory.getTestDao().find(id);
		TestReport testReport = this.daoFactory.getTestReportDao().create(test);
		/*int NumberOfSteps = test.getNumberOfSteps();
		for(int i=0;i<NumberOfSteps;i++) {
			testReport.addNextStepStepReport(test.getStepId(test.getSteps().get(i)),testReport.getStepReport(test.getStep(i)).isSuccess() , testReport.getStepReport(test.getStep(i)).getComment());
		}*/
		/*if(testReport.getNextStep() != null) {
		return "redirect:/makeReport?id=" + testReport.getId();	
		}
		else {
			
		}*/
		return "redirect:/test?id=" + id;
	}
	
/*	@RequestMapping(value = "/makeReport")
	public ModelAndView makeReportTask(@RequestParam long id) {
		ModelAndView ret = new ModelAndView("makeReport");
		TestReport testReport = daoFactory.getTestReportDao().find(id);
		Test test = testReport.getTest();
		Step nextStep = testReport.getNextStep();
		ret.addObject("id", id);
		ret.addObject("test", test);
		ret.addObject("nextStep", nextStep);
		ret.addObject("testReport", testReport);
		return ret;
	}
*/	
	/*@RequestMapping(value = "/createStepReport")
	public String createStepReport(@RequestParam(required = true) long id, @RequestParam(required = true) boolean valide, @RequestParam(required = true) String commentaire) {
		TestReport testReport = this.daoFactory.getTestReportDao().find(id);
		testReport.next(valide, commentaire);
		if(testReport.getNextStep() != null) {
			return "redirect:/makeReport?id=" + testReport.getId();	
		}
		else{
			return "redirect:/viewReport?id=" + testReport.getId();	
		}
	}*/
	
	@RequestMapping(value = "/viewReport")
	public  ModelAndView reportTask(@RequestParam long id) {
		TestReport currentReport = daoFactory.getTestReportDao().find(id);
		Test currentTest = daoFactory.getTestReportDao().find(id).getTest();
		List<Step> currentSteps = currentTest.getSteps();
		ModelAndView ret = new ModelAndView("viewReport");
		ret.addObject("test", currentTest);
		ret.addObject("testReport", currentReport);
		ret.addObject("Steps",currentSteps);
		ret.addObject("StepReports", currentReport.getStepReports());
		return ret;
	}
}