package com.userservice.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.userservice.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
   
	@Query(value="{'_id':?0,'suspended':false}")
	public User findUserById(ObjectId id);
	
	
	
}
