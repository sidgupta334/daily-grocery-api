package com.treggo.grocericaApi.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

	public Orders findByOrderId(Long orderId);

	@Query("from Orders where user_Id = :userId")
	public List<Orders> fetchByUserId(@Param("userId") Long userId);

	@Query("from Orders where order_Status = :orderStatus")
	public List<Orders> fetchByOrderStatus(@Param("orderStatus") String orderStatus);
	
	@Query("from Orders where order_Status = :orderStatus and vendor_id = :vendorId")
	public List<Orders> fetchByOrderStatusAndVendor(@Param("orderStatus") String orderStatus, @Param("vendorId") Long vendorId);

	@Query("from Orders where order_Status = :orderStatus1 or order_Status = :orderStatus2 or order_Status = :orderStatus3 or order_Status = :orderStatus4")
	public List<Orders> fetchByMultipleOrders(@Param("orderStatus1") String orderStatus1,
			@Param("orderStatus2") String orderStatus2, @Param("orderStatus3") String orderStatus3,
			@Param("orderStatus4") String orderStatus4);

	@Query("from Orders where mapped_To = :userId")
	public List<Orders> fetchByMappedUser(@Param("userId") Long userId);

	@Query("from Orders where mapped_To = :userId and order_Status = :orderStatus")
	public List<Orders> fetchByMappedUserAndStatus(@Param("userId") Long userId,
			@Param("orderStatus") String orderStatus);

	@Query("from Orders where addressId in (:addressId) and order_Status = 'COMPLETED'")
	public List<Orders> fetchByAddressId(@Param("addressId") List<Long> addressId);
	
	@Query("from Orders where addressId in (:addressId) and order_Status = 'COMPLETED' and vendor_id = :vendorId")
	public List<Orders> fetchByAddressIdAndVendor(@Param("addressId") List<Long> addressId, @Param("vendorId") Long vendorId);

	@Query("from Orders where online_payment_mode = :onlinePaymentMode and order_Status = 'COMPLETED'")
	public List<Orders> fetchByPaymentMode(@Param("onlinePaymentMode") String onlinePaymentMode);
	
	@Query("from Orders where online_payment_mode = :onlinePaymentMode and order_Status = 'COMPLETED' and vendor_id = :vendorId")
	public List<Orders> fetchByPaymentModeAndVendor(@Param("onlinePaymentMode") String onlinePaymentMode, @Param("vendorId") Long vendorId);

	@Query("from Orders where transaction_id = :transactionId and order_Status = 'COMPLETED'")
	public List<Orders> fetchByTransactionId(@Param("transactionId") String transactionId);
	
	@Query("from Orders where transaction_id = :transactionId and order_Status = 'COMPLETED' and vendor_id = :vendorId")
	public List<Orders> fetchByTransactionIdAndVendor(@Param("transactionId") String transactionId, @Param("vendorId") Long vendorId);

	@Query("from Orders where payment_method = :paymentMethod and order_Status = 'COMPLETED'")
	public List<Orders> fetchByPaymentMethod(@Param("paymentMethod") String paymentMethod);
	
	@Query("from Orders where payment_method = :paymentMethod and order_Status = 'COMPLETED' and vendor_id = :vendorId")
	public List<Orders> fetchByPaymentMethodAndVendor(@Param("paymentMethod") String paymentMethod, @Param("vendorId") Long vendorId);

	@Query("from Orders where order_date >= :startDate and order_date <= :endDate and order_Status = 'COMPLETED'")
	public List<Orders> fetchByFilterDates(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
	
	@Query("from Orders where order_date >= :startDate and order_date <= :endDate and order_Status = 'COMPLETED' and vendor_id = :vendorId")
	public List<Orders> fetchByFilterDatesAndVendor(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("vendorId") Long vendorId);

	@Query("from Orders where order_date >= :startDate and order_date <= :endDate and order_Status <> 'CANCELLED_COD' and order_Status <> 'CANCELLED_ONLINE'")
	public List<Orders> fetchAllByFilterDates(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
	
	@Query("from Orders where order_date >= :startDate and order_date <= :endDate and vendor_id = :vendorId and order_Status <> 'CANCELLED_COD' and order_Status <> 'CANCELLED_ONLINE'")
	public List<Orders> fetchAllByFilterDatesAndVendor(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("vendorId") Long vendorId);

}
