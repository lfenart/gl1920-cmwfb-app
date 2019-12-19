package fr.uha.ensisa.gl.cmwfb.mantest_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.cmwfb.mantest.TestBook;
import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;

@Controller
public class TestBookController {
	
	@Autowired
	public DaoFactory daoFactory;
	
	 @RequestMapping(value = "/books")
		public ModelAndView books() {
			ModelAndView ret = new ModelAndView("books");
			// Adds an objet to be used in home.jsp
			ret.addObject("testBooks", daoFactory.getTestBookDao().findAll());
			return ret;
		}
		
		@RequestMapping(value = "/createBook")
		public String createBook(@RequestParam(required = true) String testBookName) {
			TestBook newTestBook = new TestBook();
			long id = daoFactory.getTestBookDao().count() + 1;
			while (daoFactory.getTestBookDao().find(id) != null)
				id++;
			newTestBook.setId(id);
			newTestBook.setName(testBookName);
			daoFactory.getTestBookDao().persist(newTestBook);
			return "redirect:/books";
		}
		
		/*@RequestMapping(value = "/book")
		public ModelAndView book(@RequestParam(required = true) long testBookId) throws IOException {
			ModelAndView ret = new ModelAndView("book");
			ret.addObject("testBook", daoFactory.getTestBookDao().find(testBookId));
			ret.addObject("tests", daoFactory.getTestBookDao().find(testBookId).getTests());
			ret.addObject("state", daoFactory.getTestBookDao().find(testBookId).getState());

			return ret;
		}*/
}
