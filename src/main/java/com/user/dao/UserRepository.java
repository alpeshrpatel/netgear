package com.user.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.user.model.User;

@Repository
public class UserRepository {
	public static final String COLLECTION_NAME = "user";
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public void addUser(User user) {
        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.createCollection(User.class);
        }       
        mongoTemplate.insert(user, COLLECTION_NAME);
    }
	
	public User getUserByUsername(String username) {
	    return mongoTemplate.findOne(
	    		Query.query(Criteria.where("username").is(username)), User.class, COLLECTION_NAME);
	}
	
	public List<User> getAllUser() {
        return mongoTemplate.findAll(User.class, COLLECTION_NAME);
    }
     
    public User deleteUser(String username) {
    	User user = mongoTemplate.findOne(
	    		Query.query(Criteria.where("username").is(username)), User.class, COLLECTION_NAME);
        mongoTemplate.remove(user, COLLECTION_NAME);
        
        return user;
    }
     
    public User updateUser(String username, User user) {
    	Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
 
		Update update = new Update();
		update.set("username", user.getUsername());
		update.set("password", user.getPassword());
		update.set("email", user.getEmail());
 
        mongoTemplate.updateFirst(query, update, User.class);
        
        return user;
    }
    
    public User validateUser(String email, String password)
    {
    	Query query = new Query();
    	
    	query.addCriteria(
                Criteria.where("email").is(email).andOperator(
                      Criteria.where("password").is(password)
	            )
             );
    	return mongoTemplate.findOne(query, User.class, COLLECTION_NAME);
    }
    
}
