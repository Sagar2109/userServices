package com.userservice.daoImpl;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.userservice.dao.UserDao;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.request.ListPageRequest;

@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private UserRepository userRepo;

	@Override
	public User findUserById(String id) {

		return userRepo.findUserById(new ObjectId(id));
     
	}

	@Override
	public User addUser(User user) {

		mongoTemplate.save(user, "UserInfo");
		return user;
	}

	@Override
	public boolean isEmailExists(String email) {
		if (mongoTemplate.exists(Query.query(Criteria.where("email").is(email)), User.class)) {
			return true;
		}
		return false;
	}

	@Override
	public List<User> findAllUsers(ListPageRequest page) {

		Criteria cnm = Criteria.where("name").regex(page.getSearchText(), "i");
		Criteria sus = Criteria.where("suspended").is(false);

		Query query = new Query(new Criteria().andOperator(cnm, sus));

		query = query.skip(page.getPage() * page.getTotalInList()).limit((int) page.getTotalInList());

		return mongoTemplate.find(query, User.class, "UserInfo");
	}

	@Override
	public User updateUser(User user) {
		
		Query query = Query.query(Criteria.where("id").is(user.getId()));
		Update update = new Update().set("name", user.getName()).set("contactNo", user.getContactNo()).set("modifiedAt",
				new Date());
		//if(user.getName()!=null)
		//update.set("name",user.getName());	
		return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), User.class);

	}

}