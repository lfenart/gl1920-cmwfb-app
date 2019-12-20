package fr.uha.ensisa.gl.cmwfb.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestReportDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestSerieDao;
import fr.uha.ensisa.gl.cmwfb.mantest_app.controller.TestSerieController;

public class TestSerieControllerTest {
	@Mock
	public DaoFactory daoFactory;
	@Mock
	public TestDao daoTask;
	@Mock
	public TestReportDao daoTestReport;
	@Mock
	private TestSerieDao daoTestSerie;
	
	public TestSerieController sut;
	@Before
	public void prepareDao() {
		MockitoAnnotations.initMocks(this); // crée les @Mock
		when(daoFactory.getTestDao()).thenReturn(this.daoTask);
		when(daoFactory.getTestReportDao()).thenReturn(this.daoTestReport);
		when(daoFactory.getTestSerieDao()).thenReturn(this.daoTestSerie);
		sut = new TestSerieController(); // System Under Test
		sut.daoFactory = this.daoFactory;
	}
	
	@Test
	public void creationSerieForm() {
		ModelAndView ret = sut.creationSerieForm(1L);
		assertEquals("createTestSerie",ret.getViewName());
	}
	
	@Test
	public void createSerie() {
		Long serieId = 1L;
		String serieName="serie1";
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serie = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(1,serieName);
		when(daoTestSerie.create(serieName)).thenReturn(serie);
		String link = sut.createSerie(1L,serieName);
		assertEquals("redirect:/viewSerie?id=1",link);
		verify(daoTestSerie).create(serieName);
		verify(daoTestSerie).persist(serie);
	}
	
	@Test 
	public void addTest() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		test.setId(1);
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(1,"serie1");
		
		when(daoTask.find(1)).thenReturn(test);
		
		when(daoTestSerie.find(1)).thenReturn(serieTest);
		
		sut.addTest(1L ,1,1);
		
		verify(daoTask).find(1);
		verify(daoTestSerie).find(1);
	}
	
	@Test
	public void removeTest() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		test.setId(1);
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(1,"serie1");
		serieTest.add(test);
		
		when(daoTask.find(1)).thenReturn(test);
		when(daoTestSerie.find(1)).thenReturn(serieTest);
		String link = sut.removeTest(1L ,1L,1L);
		assertEquals("redirect:/viewSerie?id=1",link);
		
		verify(daoTask).find(1);
		verify(daoTestSerie).find(1);

	}
	
	@Test
	public void addTestSerie(){
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest1 = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(1,"serie1");
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest2 = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(2,"serie2");
		when(daoTestSerie.find(1)).thenReturn(serieTest1);
		when(daoTestSerie.find(2)).thenReturn(serieTest2);
		
		sut.addTestSerie(1L ,1, 2);
		
		verify(daoTestSerie,times(2)).find(any(Long.class));		
	}
	
	@Test
	public void removeTestSerie() {
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest1 = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(1,"serie1");
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest2 = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(2,"serie2");
		serieTest1.add(serieTest2);
		
		when(daoTestSerie.find(1)).thenReturn(serieTest1);
		when(daoTestSerie.find(2)).thenReturn(serieTest2);
		
		String link = sut.removeTestSerie(1L ,1, 2);
		assertEquals("redirect:/viewSerie?id=1",link);
		
		verify(daoTestSerie,times(2)).find(any(Long.class));	
	}
	
	@Test
	public void reportSerie() {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		test.setId(1);
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(1,"serie1");
		when(daoTask.find(1)).thenReturn(test);		
		when(daoTestSerie.find(1)).thenReturn(serieTest);
		serieTest.add(test);
		
		ModelAndView ret = sut.reportSerie(1L,1);
		assertEquals("viewSerie",ret.getViewName());
		
		fr.uha.ensisa.gl.cmwfb.mantest.Test[] tests = (fr.uha.ensisa.gl.cmwfb.mantest.Test[]) ret.getModelMap().get("tests");
		assertNotNull(tests);
		assertEquals(test,tests[0]);
		
		verify(daoTestSerie,times(2)).find(1);	
	}
	
	@Test
	public void deleteSerie() {
		fr.uha.ensisa.gl.cmwfb.mantest.TestSerie serieTest = new fr.uha.ensisa.gl.cmwfb.mantest.TestSerie(1,"serie1");
		when(daoTestSerie.find(1)).thenReturn(null,serieTest);
		
		sut.deleteSerie(1L,1L);
		sut.deleteSerie(1L,1L);
		
		verify(daoTestSerie,times(2)).find(1);	
		verify(daoTestSerie).deleteSerie(any(fr.uha.ensisa.gl.cmwfb.mantest.TestSerie.class));
	}

	
	
	
}