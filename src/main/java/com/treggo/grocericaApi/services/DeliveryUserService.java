package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.DeliveryUser;
import com.treggo.grocericaApi.entities.Session;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.enums.userType;
import com.treggo.grocericaApi.repositories.DeliveryUserRepository;
import com.treggo.grocericaApi.repositories.SessionRepository;
import com.treggo.grocericaApi.repositories.UsersRepository;
import com.treggo.grocericaApi.requests.DeliveryUserDTO;
import com.treggo.grocericaApi.requests.LoginDTO;
import com.treggo.grocericaApi.requests.OtpValidationDTO;
import com.treggo.grocericaApi.requests.SMSModel;
import com.treggo.grocericaApi.requests.UpdateUserDTO;
import com.treggo.grocericaApi.requests.updatePasswordDTO;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.responses.LoginResponse;
import com.treggo.grocericaApi.responses.UserResponse;

@Service
public class DeliveryUserService {

	@Autowired
	private DeliveryUserRepository userRepo;

	@Autowired
	private UsersRepository repo;

	@Autowired
	private PasswordService pwd;

	@Autowired
	private SessionRepository sessionRepo;

	@Autowired
	private TokenGenerator tokenService;

	@Autowired
	private OTPService otpService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsService smsService;

	Logger logger = LoggerFactory.getLogger(DeliveryUserService.class);

	// Create a new delivery user in the system:
	public UserResponse createNewUser(DeliveryUserDTO req) {
		UserResponse res = new UserResponse();
		if (req.getFullName() == null) {
			res.setValid(false);
			res.setMessage("Fullname is required");
		}

		else if (req.getEmail() == null) {
			res.setValid(false);
			res.setMessage("Email is required");
		}

		else if (req.getVendorId() == null) {
			res.setValid(false);
			res.setMessage("VendorId is required");
		}

		else if (req.getMobile() == null) {
			res.setValid(false);
			res.setMessage("Mobile is required");
		}

		else if (req.getPassword() == null) {
			res.setValid(false);
			res.setMessage("Password is required");
		}

		else {
			DeliveryUser userToSave = new DeliveryUser();
			userToSave.setCreated_on(LocalDate.now());
			BeanUtils.copyProperties(req, userToSave);
			userToSave.setUserType(userType.DELIVERY);

			userToSave.setPassword(pwd.encrypt(req.getPassword()));
			if (req.getGender().equalsIgnoreCase("female")) {
				userToSave.setGender("female");
			} else {
				userToSave.setGender("male");
			}

			try {
				Users user = repo.fetchByUserId(req.getVendorId());
				if (user == null || !user.getUserType().equals(userType.VENDOR)) {
					res.setValid(false);
					res.setMessage("Vendor not found in system");
					return res;
				} else {
					userToSave.setUser(repo.fetchByUserId(req.getVendorId()));
					if (req.getUserId() != null) {
						if (_isValidUser(req.getUserId())) {
							userToSave.setDeliveryUserId(req.getUserId());
						} else {
							res.setValid(false);
							res.setMessage("User ID is invalid");
						}
					}

					// Check if existing user exists
					DeliveryUser exists = userRepo.findByEmail(req.getEmail());
					if (exists != null && req.getUserId() == null) {
						res.setValid(false);
						res.setMessage("Email already exists");
					} else {

						// SAVE USER TO DB
						userRepo.save(userToSave);
						res.setValid(true);
						res.setMessage("Delivery User created successfully");
						BeanUtils.copyProperties(userToSave, res);
					}
				}
			} catch (Exception e) {
				logger.error("Some error occured: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return res;
	}

	public DeliveryUser updateUser(UpdateUserDTO req) {
		if (req.getFullName() == null) {
			return null;
		}

		else if (req.getEmail() == null) {
			return null;
		}

		else if (req.getMobile() == null) {
			return null;
		}

		else if (req.getUserId() == null) {
			return null;
		} else {
			if (!_isValidUser(req.getUserId())) {
				return null;
			} else {
				try {
					DeliveryUser user = userRepo.findByDeliveryUserId(req.getUserId());
					user.setDob(req.getDob());
					user.setFullName(req.getFullName());
					user.setGender(req.getGender());
					userRepo.save(user);
					return user;
				} catch (Exception e) {
					logger.error("Failed to run: " + e);
					return null;
				}
			}
		}
	}

	public List<DeliveryUser> getAllUsers() {
		List<DeliveryUser> users = userRepo.findAll();
		return _removePassword(users);
	}

	public List<DeliveryUser> getAllUsersByVendor(Long vendorId) {
		List<DeliveryUser> users = userRepo.getUsersByVendor(vendorId);
		return _removePassword(users);
	}

	public DeliveryUser findByUserId(Long userId) {
		return userRepo.findByDeliveryUserId(userId);
	}

	public DeliveryUser findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public LoginResponse loginUser(LoginDTO req) {
		try {
			DeliveryUser user = userRepo.findByEmail(req.getEmail());
			if (user == null) {
				return null;
			}

			String password = pwd.decrypt(user.getPassword());
			if (password.equals(req.getPassword())) {
				Session checkSession = sessionRepo.findByUserIdAndUserType(user.getDeliveryUserId(),
						user.getUserType().toString());
				if (checkSession != null) {
					sessionRepo.deleteSession(checkSession.getUserId(), checkSession.getUserType());
				}

				return this.generateUserSession(user);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public String updatePassword(updatePasswordDTO req) {
		if (req.getEmail() == null || req.getNewPassword() == null || req.getOldPassword() == null) {
			return "INVALID";
		}
		try {
			DeliveryUser user = userRepo.findByEmail(req.getEmail());
			String encryptOldPwd = pwd.encrypt(req.getOldPassword());
			if (encryptOldPwd.equals(user.getPassword())) {
				String encryptNew = pwd.encrypt(req.getNewPassword());
				user.setPassword(encryptNew);
				userRepo.save(user);
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("Error due to : " + e);
			return "FAILURE";
		}
		return "FAILURE";
	}

	// Send OTP on mail via forgot password
	public String forgotPassword(String email) {
		try {
			DeliveryUser user = userRepo.findByEmail(email);
			if (user == null) {
				return "NOT FOUND";
			}

			int otp = otpService.generateOTP(email);
			String text = smsService.forgetPassOTPMessage(otp);
			SMSModel sms = new SMSModel("+91" + user.getMobile(), "", text);
			smsService.sendSms(sms);
			// Send mail to user:
			boolean result = emailService.sendOtpMessage(email, Integer.toString(otp));
			if (result) {
				return "SUCCESS";
			} else {
				return "FAILURE";
			}
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return "ERROR";
		}
	}

	public boolean deleteUser(Long userId) {
		try {
			DeliveryUser user = userRepo.findByDeliveryUserId(userId);
			userRepo.delete(user);
			Session s = sessionRepo.findByUserIdAndUserType(userId, user.getUserType().toString());
			if (s != null) {
				sessionRepo.delete(s);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public GeneralResponse validateOTP(OtpValidationDTO req) {

		GeneralResponse res = new GeneralResponse();
		if (req.getEmail() == null || req.getNewPassword() == null || req.getOtp() == null) {
			res.setMessage("Invalid Request");
		}

		int otp = otpService.getOtp(req.getEmail());
		try {
			DeliveryUser user = userRepo.findByEmail(req.getEmail());
			if (user == null || !user.getEmail().equals(req.getEmail())) {
				res.setMessage("User not found");
			}

			if (otp == Integer.parseInt(req.getOtp())) {
				String password = pwd.encrypt(req.getNewPassword());
				user.setPassword(password);
				userRepo.save(user);
				res.setMessage("SUCCESS");
				return res;
			} else {
				res.setMessage("OTP doesn't matched");
				return res;
			}

		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			res.setMessage("Something went wrong");
			return res;
		}

	}
	
	public boolean logoutUser(DeliveryUser user) {
		try {
			sessionRepo.deleteSession(user.getDeliveryUserId(), user.getUserType().toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean _isValidUser(Long userId) {
		try {
			if (userRepo.findByDeliveryUserId(userId) == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return false;
		}
	}

	private List<DeliveryUser> _removePassword(List<DeliveryUser> users) {
		for (DeliveryUser u : users) {
			u.setPassword(null);
		}
		return users;
	}

	private LoginResponse generateUserSession(DeliveryUser user) {
		// Check if already session exists
		Session checkSession = sessionRepo.findByUserIdAndUserType(user.getDeliveryUserId(),
				user.getUserType().toString());
		if (checkSession != null) {
			sessionRepo.deleteSession(checkSession.getUserId(), checkSession.getUserType());
		}

		Session session = new Session();
		session.setUserId(user.getDeliveryUserId());
		session.setUserType(user.getUserType().toString());
		session.setToken(tokenService.generateToken());
		session.setCreated_on(LocalDate.now());
		try {
			sessionRepo.save(session);
		} catch (Exception e) {
			logger.error("Unable to create session for user: " + user.getDeliveryUserId());
			logger.error("Error: " + e);
			return null;
		}
		LoginResponse resp = new LoginResponse();
		resp.setDob(user.getDob());
		resp.setEmail(user.getEmail());
		resp.setFullName(user.getFullName());
		resp.setMessage("Login Success");
		resp.setMobile(user.getMobile());
		resp.setToken(session.getToken());
		resp.setUserId(user.getDeliveryUserId());
		resp.setUserType(user.getUserType());
		return resp;
	}
}
