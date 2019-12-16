package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Step;
import fr.uha.ensisa.gl.cmwfb.mantest.StepReport;
import fr.uha.ensisa.gl.cmwfb.mantest.Test;
import fr.uha.ensisa.gl.cmwfb.mantest.TestReport;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;

@Controller
public class TestReportController {

	@Autowired
	public DaoFactory daoFactory;
	

	@RequestMapping(value = "/addStep")
	private String addStep(@RequestParam(required = true) long id, @RequestParam(required = true) String stepText) {
		Test test = daoFactory.getTestDao().find(id);
		Step step = new Step();
		step.setText(stepText);
		test.addStep(step);
		return "redirect:/test?id=" + id;
	}
	
	@RequestMapping(value = "/createReport")
	public String createReport(@RequestParam(required = true) long id) {
		Test test = this.daoFactory.getTestDao().find(id);
		if (test !=null)
			this.daoFactory.getTestReportDao().create(test);
		return "redirect:/test?id=" + id;
	}
	
	@RequestMapping(value = "/viewReport")
	public  ModelAndView reportTask(@RequestParam long id) {
		TestReport currentReport = daoFactory.getTestReportDao().find(id);
		Test currentTest = daoFactory.getTestReportDao().find(id).getTest();
		List<Step> currentSteps = currentTest.getSteps();
		List<StepReport> stepReports= currentReport.getStepReports();
		ModelAndView ret = new ModelAndView("viewReport");
		ret.addObject("test", currentTest);
		ret.addObject("testReport", currentReport);
		ret.addObject("Steps",currentSteps);
		ret.addObject("StepReports", stepReports );
		return ret;
	}

	@RequestMapping(value = "/addStepReport")
	private String addStepReport(@RequestParam(required = true) long testId, @RequestParam(required = true) int stepId,
			@RequestParam(required = true) String comment, @RequestParam(required = true) String result) {
		TestReport testReport = daoFactory.getTestReportDao().find(testId);
		testReport.addNextStepStepReport(stepId, result , comment);
		return "redirect:/test?id=" + testId;
	}
	
	@RequestMapping(value = "/modifyStepReport")
	private String modifyStepReport(@RequestParam(required = true) long testId, @RequestParam(required = true) int stepId,
			@RequestParam(required = true) String comment, @RequestParam(required = true) String result) {
		TestReport testReport = daoFactory.getTestReportDao().find(testId);
		StepReport stepReport = testReport.getStepReport(stepId);
		if (stepReport!=null) {
			stepReport.setComment(comment);
			stepReport.setResult(result);
		}		
		return "redirect:/test?id=" + testId;
	}
	
}