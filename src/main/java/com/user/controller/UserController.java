package com.user.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.user.dao.UserRepository;
import com.user.model.User;
import com.user.rest.MultipleUserResponse;
import com.user.rest.RestResponse;
import com.user.rest.UserResponse;

/**
 * Handles requests for User Entity
 */
@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    private UserRepository userRepository;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Default Home REST page. The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "status";
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	@ResponseBody
	public MultipleUserResponse getAllusers() {
		logger.info("Inside getAllusers() method...");

		List<User> allusers = userRepository.getAllUser();
		MultipleUserResponse extResp = new MultipleUserResponse(true, allusers);
		
		return extResp;
	}
	
	@RequestMapping(value="/user/{username}", method=RequestMethod.GET)
	@ResponseBody
	public UserResponse getuserByUsername(@PathVariable("username") String username) {
		User user = userRepository.getUserByUsername(username);
		UserResponse userResponse;
		if (user != null) {
			userResponse = new UserResponse(true, user);
			logger.info("Inside getuserByUsername, returned: " + user.toString());
		} else {
			userResponse = new UserResponse(false, user);
			logger.info("Inside getuserByUsername, User: " + username + ", NOT FOUND!");
		}
		
		
		return userResponse; 
	}

	@RequestMapping(value="/user/delete/{username}", method=RequestMethod.DELETE)
	@ResponseBody
	public RestResponse deleteuser(@PathVariable("username") String username) {
		RestResponse restResponse;
		
		User myuser = userRepository.deleteUser(username);
		
		if (myuser != null) {
			logger.info("Inside delete, deleted: " + myuser.getUsername());
			restResponse = new RestResponse(true, "Successfully deleted user: " + myuser.getUsername());
		} else {
			logger.info("Inside delete, User: " + username + ", NOT FOUND!");
			restResponse = new RestResponse(false, "Failed to delete User: " + username);
		}
		
		return restResponse;
	}
	
	@RequestMapping(value="/user/update/{username}", method=RequestMethod.PUT)
	@ResponseBody
	public RestResponse updateUser(@PathVariable ("username") String username, @RequestBody User user) {
		
		RestResponse restResponse;
		
		User myuser = userRepository.updateUser(username, user);
		
		if (myuser != null) {
			logger.info("Inside updateUser, updated: " + myuser.toString());
			restResponse = new RestResponse(true, "Successfully updated user: " + myuser.getUsername());
		} else {
			logger.info("Inside updateUser, User: " + user + ", NOT FOUND!");
			restResponse = new RestResponse(false, "Failed to update User: " + user);
		}
		
		return restResponse;
	}

	@RequestMapping(value="/user/adduser", method=RequestMethod.POST)
	@ResponseBody
	public RestResponse adduser(@RequestBody User user) {
		RestResponse restResponse;
		
		if (user.getUsername() != null && user.getUsername().length() > 0) {
			logger.info(" adduser, adding: " + user.toString());
			userRepository.addUser(user);
			restResponse = new RestResponse(true, "Successfully added user: " + user.getUsername());
		} else {
			logger.info("Failed to insert...");
			restResponse = new RestResponse(false, "Failed to insert...");
		}
		
		return restResponse;
	}
	
	@RequestMapping(value="/validateUser", method=RequestMethod.GET)
	@ResponseBody
	public RestResponse validateUser(@RequestParam ("email") String email,@RequestParam ("password") String password) {
		
		User myuser = userRepository.validateUser(email, password);
		RestResponse restResponse = new RestResponse();
		if (myuser != null) {
			restResponse.setSuccess(true);
			restResponse.setMessage("User is validated successfully !");
			logger.info("validateUser, returned: " + myuser.toString());
		} else {
			restResponse.setSuccess(false);
			restResponse.setMessage("User is not found !");
			logger.info("validateUser, User: " + email + ", NOT FOUND!");
		}
		
		
		return restResponse; 
	}
	
}
