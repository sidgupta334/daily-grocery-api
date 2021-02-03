package com.treggo.grocericaApi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.DeliveryUser;
import com.treggo.grocericaApi.entities.Session;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.repositories.DeliveryUserRepository;
import com.treggo.grocericaApi.repositories.SessionRepository;
import com.treggo.grocericaApi.repositories.UsersRepository;

@Service
public class TokenGenerator {

	@Autowired
	private SessionRepository sessionRepo;
	
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private DeliveryUserRepository deliveryRepo;
	
	Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

	public String generateToken() {
		long count = 250;
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		return builder.toString();
	}
	
	
	public Users validateToken(String token) {
		try {
			Session s = sessionRepo.findByToken(token);
			if(s!= null) {
				Users user = userRepo.fetchByUserId(s.getUserId());
				if(user == null) {
					sessionRepo.deleteSession(s.getUserId(), s.getUserType());
					return null;
				}
				return user;
			}
		}
		catch(Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
		return null;
	}
	
	//Token validation for Delivery users
	public DeliveryUser validateDeliveryToken(String token) {
		try {
			Session s = sessionRepo.findByToken(token);
			if(s!= null) {
				DeliveryUser user = deliveryRepo.findByDeliveryUserId(s.getUserId());
				if(user == null) {
					sessionRepo.deleteSession(s.getUserId(), s.getUserType());
					return null;
				}
				return user;
			}
		}
		catch(Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
		return null;
	}
}
