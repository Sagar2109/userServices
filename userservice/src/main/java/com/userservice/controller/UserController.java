package com.userservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import com.userservice.request.ChangePasswordRequest;
import com.userservice.request.EmailDetails;
import com.userservice.request.ListPageRequest;
import com.userservice.request.UserDeleteRequest;
import com.userservice.request.UserListRequest;
import com.userservice.request.UserLoginRequest;
import com.userservice.request.UserUpdateRequest;
import com.userservice.response.UserCoursesResponse;
import com.userservice.service.EmailService;
import com.userservice.service.UserService;
import com.userservice.util.Response;
import com.userservice.util.Utils;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@GetMapping("/")
	public Object findUserById(@RequestParam String id) {

		User user;
		try {

			user = userService.findUserById(id);
			if (user == null) {

				throw new IllegalAccessException();
			} else {
				log.info("Inside UserController in Api findUserById(1111)");
				return Response.data(HttpStatus.OK.value(), "Ok", "User Found", user);
			}
		} catch (Exception e) {
			log.info("Exception Inside UserController in Api findUserById(2222)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "User Not Found", null);

		}
	}

	@PostMapping("/")
	public Object addUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors() || bindingResult.hasFieldErrors()) {
			log.info(bindingResult.toString());
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);
		}

		UserDTO dto = userService.addUser(userDTO);

		try {

			if (dto == null)
				throw new IllegalAccessException();

			return Response.data(HttpStatus.OK.value(), "OK", "User Added Successfully", dto);

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api addUser(...)");
			return Response.data(HttpStatus.CONFLICT.value(), "Conflict", "Email is taken Once", null);

		}

	}

	@PostMapping("/list")
	public Object findAllUsers(@Valid @RequestBody ListPageRequest listPageRequest, BindingResult bindingResult) {

		List<User> list = null;
		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {

			log.info("Inside UserController in Api findAllUser(eeeeeeee)" + bindingResult.toString());
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);

		}

		try {

			list = userService.findAllUsers(listPageRequest);
			if (list == null) {
				throw new IllegalAccessException();
			}
			log.info("Inside UserController in Api findAllUser(...)");
			return Response.data(HttpStatus.OK.value(), "Ok", "List Found", list);

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api findAllUser(...)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "Record Not Found", null);

		}
	}

	/*
	 * @PutMapping("/update") public Map<String, Object> updateUser(@RequestBody
	 * User user, BindingResult bindingResult) { if (bindingResult.hasErrors()||
	 * bindingResult.hasFieldErrors()) { return
	 * ResponseEntity.badRequest().body(bindingResult.getFieldError().
	 * getDefaultMessage()); } else {
	 * 
	 * userService.updateUser(user);
	 * 
	 * return ResponseEntity.ok().body(user);
	 * 
	 * } }
	 */
	@PutMapping("/update")
	public Object updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult bindingResult) {
		User u1 = null;

		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);
		}

		try {

			u1 = userService.updateUser(userUpdateRequest);

			if (u1 == null)
				throw new IllegalAccessException();

			return Response.data(HttpStatus.OK.value(), "Ok", "User Updated successfuly", u1);
		} catch (Exception e) {
			log.info("Exception Inside UserController in Api updateUser(...)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "User is not found", null);

		}
	}

	@PutMapping("/delete")
	public Object deleteUser(@Valid @RequestBody UserDeleteRequest userDeleteRequest, BindingResult bindingResult) {
		User u1 = null;

		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);
		}
		try {

			u1 = userService.deleteUser(userDeleteRequest);

			if (u1 == null)

				throw new IllegalAccessException();

			return Response.data(HttpStatus.OK.value(), "Ok", "User Deleted successfuly", u1.getId());

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api deleteUser(...)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "User is not found", u1);

		}
	}

	@GetMapping(value = "/all-courses-byuser")
	public Object findAllCoursesByUser(@Valid @RequestParam String userid) {
		UserCoursesResponse u1 = null;
		if (userid == null) {
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", "useid is null", null);
		}
		try {

			u1 = userService.findAllCoursesByUserService(userid);

			if (u1 == null)

				throw new IllegalAccessException();

			return Response.data(HttpStatus.OK.value(), "Ok", "User with List of Courses is found", u1);

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api findAllCoursesByUser(...)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "User is not found", null);

		}
	}

	@PostMapping("/users-by-ids")
	public Object findUsersByIds(@Valid @RequestBody UserListRequest request, BindingResult bindingResult) {

		List<User> userlist = null;

		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);
		}
		try {

			userlist = userService.findUsersByIds(request);

			if (userlist == null)

				throw new IllegalAccessException();

			return Response.data(HttpStatus.OK.value(), "Ok", "User list by ids found", userlist);

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api findUsersByIds(...)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "User list by ids not found", userlist);

		}

	}

	@PostMapping("/send-mail")
	public Object sendMail(@Valid @RequestBody EmailDetails request, BindingResult bindingResult) {

		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);
		}
		try {
			boolean b1 = emailService.sendSimpleMail(request);
			if (b1 == true)
				return "Mail Sent Successfully...";
			else
				throw new IllegalAccessException();

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api sendMail(...)" + e);
			return "Error while Sending Mail";
		}

	}

	@PostMapping("/login")
	public Object userLogin(@Valid @RequestBody UserLoginRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);
		}
		try {

			User user = userService.userLoginCheck(request);

			if (user == null)
				return Response.data(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", "Email or Password not Matched",
						null);
			else
				return Response.data(HttpStatus.OK.value(), "Ok", "User logged in Successfully", user);

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api userLogin(...)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "User not found", null);

		}

	}

	@PostMapping("/change-password")
	public Object changePassword(@Valid @RequestBody ChangePasswordRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
			return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult),
					null);
		}
		try {

			User user = userService.changePassword(request);

			if (user == null)
				return Response.data(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", "Email or Password not Matched",
						null);
			else
				return Response.data(HttpStatus.OK.value(), "Ok", "Password Changed Successfully", user);

		} catch (Exception e) {
			log.info("Exception Inside UserController in Api changePassword(...)");
			return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "User not found", null);

		}

	}

}
