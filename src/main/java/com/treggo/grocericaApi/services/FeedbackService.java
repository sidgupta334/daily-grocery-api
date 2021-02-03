package com.treggo.grocericaApi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Feedback;
import com.treggo.grocericaApi.repositories.FeedbackRepository;
import com.treggo.grocericaApi.repositories.OrderRepository;
import com.treggo.grocericaApi.requests.FeedbackDTO;

@Service
public class FeedbackService {

	@Autowired
	private FeedbackRepository repo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	Logger logger = LoggerFactory.getLogger(FeedbackService.class);
	
	public Feedback createFeedback(FeedbackDTO dto) {
		if(dto.getOrderId() == null || dto.getDeliveryFeedback() == null || dto.getItemsFeedback() == null || dto.getFeedback() == null) {
			return null;
		}
		Feedback res = new Feedback();
		BeanUtils.copyProperties(dto, res);
		try {
			Feedback f = repo.fetchByOrder(dto.getOrderId());
			if(f == null) {
				res.setOrder(orderRepo.findByOrderId(dto.getOrderId()));
				repo.save(res);
				logger.info("Feedback saved for order: " + dto.getOrderId());
				return res;
			} else {
				return null;
			}
		}
		catch(Exception e) {
			logger.error("Unable to save feedback for order: " + dto.getOrderId());
			e.printStackTrace();
			return null;
		}
	}
	
	public Feedback getFeedback(Long orderId) {
		if(orderId == null) {
			return null;
		}
		try {
			return repo.fetchByOrder(orderId);
		}
		catch(Exception e) {
			logger.error("Error while fetching feedback for order: " + orderId);
			return null;
		}
	}
	
}
