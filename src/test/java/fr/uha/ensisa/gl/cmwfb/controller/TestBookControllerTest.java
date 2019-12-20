package fr.uha.ensisa.gl.cmwfb.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.TestBook;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestBookDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestReportDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestSerieDao;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.mem.TestBookDaoMem;
import fr.uha.ensisa.gl.cmwfb.mantest_app.controller.TestBookController;

public class TestBookControllerTest {
	@Mock
	public DaoFactory daoFactory;
	@Mock
	public TestDao daoTask;
	@Mock
	public TestReportDao daoTestReport;
	public TestBookController sut;
	@Mock
	private TestSerieDao daoTestSerie;
	@Mock
	private TestBookDao daoTestBook;

	@Before
	public void prepareDao() {
		MockitoAnnotations.initMocks(this); // crée les @Mock
		when(daoFactory.getTestDao()).thenReturn(this.daoTask);
		sut = new TestBookController(); // System Under Test
		sut.daoFactory = this.daoFactory;
		when(daoFactory.getTestReportDao()).thenReturn(this.daoTestReport);
		when(daoFactory.getTestSerieDao()).thenReturn(this.daoTestSerie);
		when(daoFactory.getTestBookDao()).thenReturn(this.daoTestBook);
		TestBook testBook = new TestBook();
		testBook.setId(1);
		when(daoTestBook.find(1)).thenReturn(testBook);
	}

	@Test
	public void emptyBooks() {
		ModelAndView ret = sut.books();
		Collection<fr.uha.ensisa.gl.cmwfb.mantest.TestBook> testBooks = (Collection<fr.uha.ensisa.gl.cmwfb.mantest.TestBook>) ret
				.getModelMap().get("testBooks");
		assertNotNull(testBooks);
		assertTrue(testBooks.isEmpty());
	}
	
	@Test
	public void createBook() {
		TestBookDao testBookDao = new TestBookDaoMem();
		when(daoFactory.getTestBookDao()).thenReturn(testBookDao);
		assertEquals("redirect:/books", sut.createBook("bookName"));
		assertNotNull(testBookDao.find(1));
	}
	
	@Test
	public void deleteBook() {
		TestBook testBook = new TestBook();
		when(daoTestBook.find(1)).thenReturn(testBook);
		assertEquals("redirect:/books", sut.deleteBook(1));
		verify(daoTestBook).find(1);
		verify(daoTestBook).remove(testBook);
	}

}
