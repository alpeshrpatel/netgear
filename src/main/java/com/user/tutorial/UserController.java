package com.user.tutorial;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.user.dao.UserRepository;
import com.user.model.User;
import com.user.rest.MultipleUserResponse;
import com.user.rest.RestResponse;
import com.user.rest.UserResponse;

/**
 * Handles requests for the application home page.
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
	
	@RequestMapping(value="/user/{User}", method=RequestMethod.GET)
	@ResponseBody
	public UserResponse getuserByUsername(@PathVariable("username") String username) {
		User myuser = userRepository.getUserByUsername(username);
		
		if (myuser != null) {
			logger.info("Inside getuserByUsername, returned: " + myuser.toString());
		} else {
			logger.info("Inside getuserByUsername, User: " + username + ", NOT FOUND!");
		}
		
		UserResponse extResp = new UserResponse(true, myuser);
		return extResp; 
	}

	@RequestMapping(value="/user/delete/{User}", method=RequestMethod.DELETE)
	@ResponseBody
	public RestResponse deleteuser(@PathVariable("User") String username) {
		RestResponse extResp;
		
		User myuser = userRepository.deleteUser(username);
		
		if (myuser != null) {
			logger.info("Inside deleteuserByUsername, deleted: " + myuser.toString());
			extResp = new RestResponse(true, "Successfully deleted user: " + myuser.toString());
		} else {
			logger.info("Inside deleteuserByUsername, User: " + username + ", NOT FOUND!");
			extResp = new RestResponse(false, "Failed to delete User: " + username);
		}
		
		return extResp;
	}
	
	@RequestMapping(value="/user/update/{User}", method=RequestMethod.PUT)
	@ResponseBody
	public RestResponse updateuserByTicker(@PathVariable("User") String userName, @ModelAttribute("user") User user) {
		RestResponse extResp;
		
		User myuser = userRepository.updateUser(userName, user);
		
		if (myuser != null) {
			logger.info("Inside updateuserByTicker, updated: " + myuser.toString());
			extResp = new RestResponse(true, "Successfully updated user: " + myuser.toString());
		} else {
			logger.info("Inside updateuserByTicker, User: " + user + ", NOT FOUND!");
			extResp = new RestResponse(false, "Failed to update User: " + user);
		}
		
		return extResp;
	}

	@RequestMapping(value="/user/adduser", method=RequestMethod.POST)
	@ResponseBody
	public RestResponse adduser(@ModelAttribute("user") User user) {
		RestResponse extResp;
		
		if (user.getUsername() != null && user.getUsername().length() > 0) {
			logger.info("Inside adduser, adding: " + user.toString());
			userRepository.addUser(user);
			extResp = new RestResponse(true, "Successfully added user: " + user.getUsername());
		} else {
			logger.info("Failed to insert...");
			extResp = new RestResponse(false, "Failed to insert...");
		}
		
		return extResp;
	}	
}
