package com.jakduk.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.jakduk.service.SearchService;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping(value = "/board")
	public String list(Model model,
			@RequestParam(required = false) String q) {
		
		searchService.getSearchBoard(model, q);
		
		return "search/board";
	}
	
	@RequestMapping(value = "/data/board")
	public void dataBoardList(Model model,
			@RequestParam(required = true) String q) {
		
		searchService.searchDataOfBoard(model, q);
		
	}
}
