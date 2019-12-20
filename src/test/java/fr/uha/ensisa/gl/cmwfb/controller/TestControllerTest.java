package fr.uha.ensisa.gl.cmwfb.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.Step;
import fr.uha.ensisa.gl.cmwfb.mantest.TestSerie;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestReportDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestSerieDao;
import fr.uha.ensisa.gl.cmwfb.mantest_app.controller.TestController;

public class TestControllerTest {
	@Mock
	public DaoFactory daoFactory;
	@Mock
	public TestDao daoTask;
	@Mock
	public TestReportDao daoTestReport;
	public TestController sut;
	@Mock
	private TestSerieDao daoTestSerie;

	@Before
	public void prepareDao() {
		MockitoAnnotations.initMocks(this); // crée les @Mock
		when(daoFactory.getTestDao()).thenReturn(this.daoTask);
		sut = new TestController(); // System Under Test
		sut.daoFactory = this.daoFactory;
		when(daoFactory.getTestReportDao()).thenReturn(this.daoTestReport);
		when(daoFactory.getTestSerieDao()).thenReturn(this.daoTestSerie);
	}

	/*@Test
	public void emptyList() throws IOException {
		ModelAndView ret = sut.list();
		Collection<fr.uha.ensisa.gl.cmwfb.mantest.Test> tests = (Collection<fr.uha.ensisa.gl.cmwfb.mantest.Test>) ret
				.getModelMap().get("tests");
		assertNotNull(tests);
		assertTrue(tests.isEmpty());
	}

	@Test
	public void createTest() throws IOException {
		sut.create("test");
		verify(daoTask).persist(any(fr.uha.ensisa.gl.cmwfb.mantest.Test.class));
		verify(daoTask).find(1);
	}*/

	@Test
	public void modifyTestName() throws IOException {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = mock(fr.uha.ensisa.gl.cmwfb.mantest.Test.class);
		daoTask.persist(test);
		when(daoTask.find(1)).thenReturn(test);
		sut.TestModifiedName(1, 1, "name");
		verify(daoTask).persist(any(fr.uha.ensisa.gl.cmwfb.mantest.Test.class));
		verify(daoTask).find(1);
		verify(test).setName("name");
	}
	
	@Test
	public void modifyTestStep() throws IOException {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = mock(fr.uha.ensisa.gl.cmwfb.mantest.Test.class);
		Step step = mock(Step.class);
		daoTask.persist(test);
		when(daoTask.find(1)).thenReturn(test);
		when(test.getStep(0)).thenReturn(step);
		sut.TestModifiedStep(1, 1, 0, "stepname","steptext");
		verify(daoTask).persist(any(fr.uha.ensisa.gl.cmwfb.mantest.Test.class));
		verify(daoTask).find(1);
		verify(step).setName("stepname");
		verify(step).setText("steptext");
	}

	@Test
	public void testTestNull() throws IOException {
		sut.test(1, 1);
		verify(daoTask).find(1);
	}
	
	@Test
	public void testTest() throws IOException {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		when(daoTask.find(1)).thenReturn(test);
		sut.test(1, 1);
		verify(daoTask).find(1);
	}

	@Test
	public void delete() throws IOException {
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		test.setId(1);
		daoTask.persist(test);
		when(daoTask.count()).thenReturn(0L, 1L);
		when(daoTask.find(1)).thenReturn(test);
		when(daoTestSerie.findAll()).thenReturn(new ArrayList<TestSerie>());
		//sut.delete(1L);
		verify(daoTask).persist(any(fr.uha.ensisa.gl.cmwfb.mantest.Test.class));
		verify(daoTask).remove(any(fr.uha.ensisa.gl.cmwfb.mantest.Test.class));
	}
	
	@Test 
	public void testAddDeleteStep(){
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		int testId=1;
		test.setId(testId);
		when(daoTask.find(1)).thenReturn(null,test,test);
		assertEquals("redirect:/modify?id="+testId , sut.testDeleteStep(1,testId, 0));
		daoTask.persist(test);
		sut.addStep(1,1, "step1", "");
		verify(daoTask).persist(test);
		verify(daoTask,times(2)).find(1);
	}
	
	@Test
	public void modify() throws IOException{
		fr.uha.ensisa.gl.cmwfb.mantest.Test test = new fr.uha.ensisa.gl.cmwfb.mantest.Test();
		int testId=1;
		test.setId(testId);
		when(daoTask.find(1)).thenReturn(null,test);
		ModelAndView ret = this.sut.modify(1,testId);
		assertEquals("notest",ret.getViewName());
		daoTask.persist(test);
		ret = this.sut.modify(1,testId);
		assertEquals("modify",ret.getViewName());
		
		verify(daoTask).persist(test);
	}
	
	@Test
	public void createTestInvalidId() throws IOException {
		when(daoTask.find(2)).thenReturn(mock(fr.uha.ensisa.gl.cmwfb.mantest.Test.class));
		when(daoTask.count()).thenReturn(1L);
		sut.create(1, "test");
		verify(daoTask).persist(any(fr.uha.ensisa.gl.cmwfb.mantest.Test.class));
		verify(daoTask).find(3);
	}
}