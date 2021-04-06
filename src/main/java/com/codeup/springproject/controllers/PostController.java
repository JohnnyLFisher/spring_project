package com.codeup.springproject.controllers;

import com.codeup.springproject.models.Post;
import com.codeup.springproject.models.User;
import com.codeup.springproject.repo.PostRepository;
import com.codeup.springproject.repo.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

	private final PostRepository postDao;
	private final UserRepository userDao;

	public PostController(PostRepository postDao, UserRepository userDao){
		this.userDao = userDao;
		this.postDao = postDao;
	}

//    List<Post> posts = new ArrayList<>();

	@GetMapping("/posts")
	public String seeAllPosts(Model viewModel){
		List<Post> postsFromDB = postDao.findAll();
		viewModel.addAttribute("posts", postsFromDB);
		// do not use a / to reference a template
		return "posts/index";
	}

	@GetMapping("/posts/{id}")
	public String showOnePost(@PathVariable Long id, Model vModel){
		vModel.addAttribute("post", postDao.getOne(id));
		return "posts/show";
	}

	@GetMapping("/posts/create")
	public String viewPostForm(Model vModel){
		vModel.addAttribute("post",new Post());
		return "posts/create";
	}

	@PostMapping("/posts/create")
	public String createPost(@ModelAttribute Post postToSave){

		User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// set the user
		postToSave.setOwner(userToAdd);

		// Now lets save our post;
		postDao.save(postToSave);

		return "redirect:/posts";
	}

	@GetMapping("/posts/{id}/edit")
	public String viewEditForm(Model vModel, @PathVariable Long id){
		vModel.addAttribute("post",postDao.getOne(id));
		return "posts/create";
	}

	@PostMapping("/posts/{id}/edit")
	public String editPost(@ModelAttribute Post postToUpdate, @PathVariable Long id){

		User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		System.out.println();

		postToUpdate.setId(id);

		// set the user
		postToUpdate.setOwner(userToAdd);

		// Now lets save our post;
		postDao.save(postToUpdate);

		return "redirect:/posts";
	}

}