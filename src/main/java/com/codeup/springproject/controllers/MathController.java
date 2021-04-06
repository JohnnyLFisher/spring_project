package com.codeup.springproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MathController {

	@GetMapping("/add/{numOne}/and/{numTwo}")
	@ResponseBody
	public int add(@PathVariable int numOne, @PathVariable int numTwo){
		return numOne + numTwo;
	}

//    /subtract/3/from/10


	@GetMapping("/subtract/{numOne}/from/{numTwo}")
	@ResponseBody
	public int subtract(@PathVariable int numOne, @PathVariable int numTwo){
		return numTwo - numOne;
	}

//    /multiply/4/and/5

	@GetMapping("/multiply/{numOne}/and/{numTwo}")
	@ResponseBody
	public int product(@PathVariable int numOne, @PathVariable int numTwo){
		return numTwo * numOne;
	}

//    /divide/6/by/3

	@GetMapping("/divide/{numOne}/by/{numTwo}")
	@ResponseBody
	public float divide(@PathVariable float numOne, @PathVariable float numTwo){
		return numOne/numTwo;
	}
}