package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dataobject.PrintDO;
import com.example.db.DBService;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;

@Controller
public class Greeting {

	@Autowired
	private DBService objDBService;

	@RequestMapping("/greeting")
	@ResponseBody
	public PrintDO greeting() {
		PrintDO obj = objDBService.insertAndDisplaySampleData();
		return obj;
	}

	@RequestMapping("/sachtranslate/{languagename}/{text}")
	@ResponseBody
	public String sachtranslate(@PathVariable String text, @PathVariable String languagename) {
		
		System.out.println("Text received is: " + text);
		String outText = translateToSpanish(text, languagename);
		System.out.println("Text translated is: " + outText);
		return outText;
	}

	private String translateToSpanish(String text, String languagename) {

		LanguageTranslator service = new LanguageTranslator();
		service.setUsernameAndPassword("44a75538-ab18-4f56-acc1-e2d27d8ab6db", "yNaUTJUOkYrL");
		TranslationResult translationResult = service.translate(text, Language.ENGLISH, Language.valueOf(languagename)).execute();
		return translationResult.getFirstTranslation();
	}
}
