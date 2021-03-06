package com.codeup.springproject;

import com.codeup.springproject.models.Ad;
import com.codeup.springproject.models.User;
import com.codeup.springproject.repo.AdRepository;
import com.codeup.springproject.repo.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringProjectApplication.class)
@AutoConfigureMockMvc
public class AdsIntegrationTests {

	private User testUser;
	private HttpSession httpSession;

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserRepository userDao;

	@Autowired
	AdRepository adsDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// To setup any existing users, dummy data, prepare for testing
	@Before
	public void setup() throws Exception {

		testUser = userDao.findByUsername("testUser");

		// Creates the test user if not exists
		if(testUser == null){
			User newUser = new User();
			newUser.setUsername("testUser");
			newUser.setPassword(passwordEncoder.encode("pass"));
			newUser.setEmail("testUser@codeup.com");
			testUser = userDao.save(newUser);
		}

		// Throws a Post request to /login and expect a redirection to the Ads index page after being logged in
		httpSession = this.mvc.perform(post("/login").with(csrf())
				.param("username", "testUser")
				.param("password", "pass"))
				.andExpect(status().is(HttpStatus.FOUND.value()))
				.andExpect(redirectedUrl("/ads"))
				.andReturn()
				.getRequest()
				.getSession();
	}

	@Test
	public void contextLoads() {
		// Sanity Test, just to make sure the MVC bean is working
		assertNotNull(mvc);
	}

	@Test
	public void testIfUserSessionIsActive() throws Exception {
		// It makes sure the returned session is not null
		assertNotNull(httpSession);
	}

	@Test
	public void testCreateAd() throws Exception {
		// Makes a Post request to /ads/create and expect a redirection to the Ad
		this.mvc.perform(
				post("/ads/create").with(csrf())
						.session((MockHttpSession) httpSession)
						// Add all the required parameters to your request like this
						.param("title", "new ps5")
						.param("description", "for sale $$"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	public void testShowAd() throws Exception {

		// get the first add
		Ad existingAd = adsDao.findAll().get(0);

		// Makes a Get request to /ads/{id} and expect a redirection to the Ad show page
		this.mvc.perform(get("/ads/" + existingAd.getId()))
				.andExpect(status().isOk())
				// Test the dynamic content of the page
				.andExpect(content().string(containsString(existingAd.getDescription())));
	}

	@Test
	public void testAdsIndex() throws Exception {
		Ad existingAd = adsDao.findAll().get(0);

		// Makes a Get request to /ads and verifies that we get some of the static text of the ads/index.html template and at least the title from the first Ad is present in the template.
		this.mvc.perform(get("/ads"))
				.andExpect(status().isOk())
				// Test the static content of the page d
				.andExpect(content().string(containsString("Latest ads")))
				// Test the dynamic content of the page
				.andExpect(content().string(containsString(existingAd.getTitle())));
	}

	@Test
	public void testEditAd() throws Exception {
		// Gets the first Ad for tests purposes
		Ad existingAd = adsDao.findAll().get(0);

		// Makes a Post request to /ads/{id}/edit and expect a redirection to the Ad show page
		this.mvc.perform(
				post("/ads/" + existingAd.getId() + "/update").with(csrf())
						.session((MockHttpSession) httpSession)
						.param("title", "edited title")
						.param("description", "edited description"))
				.andExpect(status().is3xxRedirection());

		// Makes a GET request to /ads/{id} and expect a redirection to the Ad show page
		this.mvc.perform(get("/ads/" + existingAd.getId()))
				.andExpect(status().isOk())
				// Test the dynamic content of the page
				.andExpect(content().string(containsString("edited title")))
				.andExpect(content().string(containsString("edited description")));
	}

}