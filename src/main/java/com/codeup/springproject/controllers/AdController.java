package com.codeup.springproject.controllers;

import com.codeup.springproject.models.Ad;
import com.codeup.springproject.models.Post;
import com.codeup.springproject.models.User;
import com.codeup.springproject.repo.AdRepository;
import com.codeup.springproject.repo.UserRepository;
import com.codeup.springproject.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdController {

	@Autowired
	private EmailService emailService;

	private final AdRepository adDao;
	private final UserRepository userDao;

	AdController(AdRepository adDao, UserRepository userDao){
		this.adDao = adDao;
		this.userDao = userDao;
	}

	@GetMapping("/ads")
	public String seeAllAds(Model viewModel){
		List<Ad> adsFromDB = adDao.findAll();
		viewModel.addAttribute("ads", adsFromDB);
		// do not use a / to reference a template
		return "ads/index";
	}

	@GetMapping("/ads/{id}")
	public String showOneAd(@PathVariable Long id, Model vModel){
		vModel.addAttribute("ad", adDao.getOne(id));
		return "ads/show";
	}

	@GetMapping("/ads/create")
	public String viewAdForm(Model model){
		model.addAttribute("ad", new Ad());
		return "ads/create";
	}

	@PostMapping("/ads/create")
	public String createAd(@ModelAttribute Ad adToBeSaved){
		User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// set the user
		adToBeSaved.setOwner(userToAdd);
		// Now lets save our post;
		adDao.save(adToBeSaved);
		emailService.prepareAndSend(adToBeSaved, "New Ad!", "A new Ad has been created in the app");
		return "redirect:/ads";
	}

	@GetMapping("/ads/{id}/update")
	public String updateAdForm(@PathVariable Long id, Model model){

		Ad adFromDb = adDao.getOne(id);

		model.addAttribute("oldAd",adFromDb);

		return "ads/update";
	}

	@PostMapping("/ads/{id}/update")
	public String updateAd(@PathVariable Long id, @ModelAttribute Ad adToUpdate){

		User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		adToUpdate.setId(id);

		// set the user
		adToUpdate.setOwner(userToAdd);

		// Now lets save our ad;
		adDao.save(adToUpdate);

		return "redirect:/ads";
	}

	@PostMapping("/ads/{id}/delete")
	@ResponseBody
	public String deleteAd(@PathVariable Long id){
		adDao.deleteById(id);
		return "You deleted an ad.";

	}



}