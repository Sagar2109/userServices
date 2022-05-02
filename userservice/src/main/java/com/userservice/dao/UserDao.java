package com.userservice.dao;

import java.util.List;

import com.userservice.model.User;
import com.userservice.request.ListPageRequest;

public interface UserDao {

	public User findUserById(String id);

	public User addUser(User user);
   
	public boolean isEmailExists(String email);

	public List<User> findAllUsers(ListPageRequest page);

	public User updateUser(User user); 
}
