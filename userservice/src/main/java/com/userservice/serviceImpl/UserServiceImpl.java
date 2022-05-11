package com.userservice.serviceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.userservice.dao.UserDao;
import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import com.userservice.request.ChangePasswordRequest;
import com.userservice.request.ListPageRequest;
import com.userservice.request.UserDeleteRequest;
import com.userservice.request.UserListRequest;
import com.userservice.request.UserLoginRequest;
import com.userservice.request.UserUpdateRequest;
import com.userservice.response.CoursesResponse;
import com.userservice.response.UserCoursesResponse;
import com.userservice.service.UserService;

@Service

public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;
    
    
	@Autowired
	private UserDao userDao;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${courseService.course.getAll}")
	private String getCourseURL;

	@Override
	public User findUserById(String id) {

		return userDao.findUserById(id);

	}

	@Override
	public UserDTO addUser(UserDTO userDTO) {

		if (userDao.isEmailExists(userDTO.getEmail())) {
			return null;

		} else {

			String s1 = passwordEncoder.encode(userDTO.getPassword());

			User user = userDTOToUser(userDTO);
			user.setPassword(s1);
			userDao.addUser(user);
			userDTO.setId(user.getId());

		}
		return userDTO;

	}

	@Override
	public List<User> findAllUsers(ListPageRequest listRequest) {

		return userDao.findAllUsers(listRequest);
	}

	@Override
	public User updateUser(UserUpdateRequest userUpdateRequest) {

		User u1 = userDao.updateUser(modelMapper.map(userUpdateRequest, User.class));

		return u1;
	}

	@Override
	public User deleteUser(UserDeleteRequest userDeleteRequest) {

		User u1 = userDao.findUserById(userDeleteRequest.getId());

		if (u1 != null && (u1.isSuspended() == false)) {
			u1.setCreatedAt(u1.getCreatedAt());
			u1.setModifiedAt(new Date());
			u1.setSuspended(true);
			return userDao.updateUser(u1);

		} else {
			return null;
		}
	}

	public User userDTOToUser(UserDTO userDto) {
		User user = modelMapper.map(userDto, User.class);
		return user;
	}

	public UserDTO userToUserDTO(User user) {
		UserDTO dto = modelMapper.map(user, UserDTO.class);

		return dto;
	}

	@Override
	public UserCoursesResponse findAllCoursesByUserService(String userid) {

		UserCoursesResponse ucs;

		User user = findUserById(userid);

		HttpHeaders headers = new HttpHeaders();
		// Map<String, Object> map = new HashMap<>();
		getCourseURL += "?createdBy=" + userid;

		headers.setAcceptLanguageAsLocales(Arrays.asList(Locale.ENGLISH));
		HttpEntity<CoursesResponse> httpEntity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<CoursesResponse>> surveyResponse = restTemplate.exchange(getCourseURL, HttpMethod.GET,
				httpEntity, new ParameterizedTypeReference<List<CoursesResponse>>() {
				});

		List<CoursesResponse> courselist = surveyResponse.getBody();

		ucs = modelMapper.map(user, UserCoursesResponse.class);
		ucs.setCourses(courselist);

		return ucs;
	}

	@Override
	public List<User> findUsersByIds(UserListRequest request) {
		return userDao.findAllUsersByIds(request);

	}

	@Override
	public User userLoginCheck(UserLoginRequest request) throws IllegalAccessException {
		User user = userDao.findUserByEmail(request.getEmail());
		boolean b;
		if (user != null) {
			b = passwordEncoder.matches(request.getPassword(), user.getPassword());
			if (b == true)
				return user;
			else
				return null;
		} else {
			log.info("Exception Inside UserServiceImpl in service userLoginCheck(...)");
			throw new IllegalAccessException();
		}
	}

	@Override
	public User changePassword(ChangePasswordRequest request) throws IllegalAccessException {

		User user = userDao.findUserByEmail(request.getEmail());
		boolean b;
		String newpass = null;

		if (user != null) {
			b = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
			if (b == true) {
				newpass = passwordEncoder.encode(request.getNewPassword());
				user.setPassword(newpass);
				return userDao.updatePassword(user);
			} else
				return null;
		} else {
			log.info("Exception Inside UserServiceImpl in service changePassword(...)");
			throw new IllegalAccessException();
		}

	}

}
