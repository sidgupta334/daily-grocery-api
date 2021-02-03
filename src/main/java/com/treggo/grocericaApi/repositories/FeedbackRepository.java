package com.treggo.grocericaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
	
	public Feedback findByFeedbackId(Long feedbackId);
	
	@Query("from Feedback where order_id = :orderId")
	public Feedback fetchByOrder(@Param("orderId") Long orderId);
	
}
