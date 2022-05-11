package com.userservice.dao;

import java.util.List;

import com.userservice.model.User;
import com.userservice.request.ListPageRequest;
import com.userservice.request.UserListRequest;

public interface UserDao {

	public User findUserById(String id);

	public User addUser(User user);
   
	public boolean isEmailExists(String email);

	public List<User> findAllUsers(ListPageRequest page);

	public User updateUser(User user);

	public List<User> findAllUsersByIds(UserListRequest request);

	public User findUserByEmail(String email);

	User updatePassword(User user); 
}
