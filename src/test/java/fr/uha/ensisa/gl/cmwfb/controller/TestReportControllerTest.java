package fr.uha.ensisa.gl.cmwfb.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Step;
import fr.uha.ensisa.gl.cmwfb.mantest.TestReport;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestBookDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestReportDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestSerieDao;
import fr.uha.ensisa.gl.cmwfb.mantest_app.controller.TestReportController;

public class TestReportControllerTest {
	@Mock
	public DaoFactory daoFactory;
	@Mock
	public TestDao daoTask;
	@Mock
	public TestReportDao daoTestReport;
	public TestReportController sut;
	@Mock
	private TestSerieDao daoTestSerie;
	@Mock
	private TestBookDao daoTestBook;

	@Before
	public void prepareDao() {
		MockitoAnnotations.initMocks(this); // crée les @Mock
		when(daoFactory.getTestDao()).thenReturn(this.daoTask);
		sut = new TestReportController(); // System Under Test
		sut.daoFactory = this.daoFactory;
		when(daoFactory.getTestReportDao()).thenReturn(this.daoTestReport);
		when(daoFactory.getTestSerieDao()).thenReturn(this.daoTestSerie);
		when(daoFactory.getTestBookDao()).thenReturn(this.daoTestBook);
	}

	@Test
	public void createReport() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		TestReport report = new TestReport(1, test);
		when(daoTask.find(1)).thenReturn(test);
		when(daoTestReport.create(test)).thenReturn(report);
		assertEquals("redirect:/test?id=" + 1 + "&testBookId=" + 1, sut.createReport(1, 1));
		verify(daoTestReport).create(test);
	}

	@Test
	public void createReportStep() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		test.addStep(new Step());
		TestReport report = new TestReport(1, test);
		when(daoTask.find(1)).thenReturn(test);
		when(daoTestReport.create(test)).thenReturn(report);
		assertEquals("redirect:/makeReport?testBookId=" + 1 + "&id=" + 1, sut.createReport(1, 1));
		verify(daoTestReport).create(test);
	}

	@Test
	public void createReportNoTest() {
		assertEquals("redirect:/test?id=" + 1 + "&testBookId=" + 1, sut.createReport(1, 1));
	}

	@Test
	public void makeReport() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		fr.uha.ensisa.gl.cmwfb.mantest.TestBook testBook = new fr.uha.ensisa.gl.cmwfb.mantest.TestBook();
		when(daoTestBook.find(1)).thenReturn(testBook);
		TestReport report = new TestReport(1, test);
		when(daoTestReport.find(1)).thenReturn(report);
		ModelAndView ret = sut.makeReportTask(1, 1);
		verify(daoFactory).getTestReportDao();
		fr.uha.ensisa.gl.cmwfb.mantest.Test test2 = (fr.uha.ensisa.gl.cmwfb.mantest.Test) ret.getModelMap().get("test");
		assertNotNull(test2);
	}

	@Test
	public void addStepReport() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		fr.uha.ensisa.gl.cmwfb.mantest.TestBook testBook = new fr.uha.ensisa.gl.cmwfb.mantest.TestBook();
		when(daoTestBook.find(1)).thenReturn(testBook);
		TestReport report = new TestReport(1, test);
		when(daoTestReport.find(1)).thenReturn(report);
		assertEquals("redirect:/viewReport?testBookId=" + 1 + "&id=" + 1, sut.addStepReport(1, 1, "comment", false));
	}

	@Test
	public void addStepReportStep() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		test.addStep(new Step());
		test.addStep(new Step());
		TestReport report = new TestReport(1, test);
		when(daoTestReport.find(1)).thenReturn(report);
		assertEquals("redirect:/makeReport?testBookId=" + 1 + "&id=" + 1, sut.addStepReport(1, 1, "comment", true));
	}

	@Test
	public void viewReport() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		TestReport report = new TestReport(1, test);
		when(daoTestReport.find(1)).thenReturn(report);
		report.setCalendar(mock(Calendar.class));
		ModelAndView ret = sut.reportTask(1, 1);
		verify(daoFactory).getTestReportDao();
		fr.uha.ensisa.gl.cmwfb.mantest.Test test2 = (fr.uha.ensisa.gl.cmwfb.mantest.Test) ret.getModelMap().get("test");
		assertNotNull(test2);
	}

}
