package com.userservice.service;

import java.util.List;

import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import com.userservice.request.ListPageRequest;
import com.userservice.request.UserDeleteRequest;
import com.userservice.request.UserUpdateRequest;
import com.userservice.response.UserCoursesResponse;

public interface UserService {

	public User findUserById(String id);

	public User updateUser(UserUpdateRequest userUpdateRequest);

	List<User> findAllUsers(ListPageRequest listRequest);

	public User deleteUser(UserDeleteRequest userDeleteRequest);

	UserDTO addUser(UserDTO userDTO);

	public UserCoursesResponse findAllCoursesByUserService(String userid);

}
