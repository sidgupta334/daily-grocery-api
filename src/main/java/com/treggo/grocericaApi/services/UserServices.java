package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Address;
import com.treggo.grocericaApi.entities.Coupon;
import com.treggo.grocericaApi.entities.Product;
import com.treggo.grocericaApi.entities.Session;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.entities.Vendor;
import com.treggo.grocericaApi.enums.userType;
import com.treggo.grocericaApi.repositories.CouponRepository;
import com.treggo.grocericaApi.repositories.ProductRepository;
import com.treggo.grocericaApi.repositories.SessionRepository;
import com.treggo.grocericaApi.repositories.UsersRepository;
import com.treggo.grocericaApi.requests.LoginDTO;
import com.treggo.grocericaApi.requests.OtpValidationDTO;
import com.treggo.grocericaApi.requests.RegisterOTPValidation;
import com.treggo.grocericaApi.requests.SMSModel;
import com.treggo.grocericaApi.requests.UpdateUserDTO;
import com.treggo.grocericaApi.requests.UserDTO;
import com.treggo.grocericaApi.requests.updatePasswordDTO;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.responses.LoginResponse;
import com.treggo.grocericaApi.responses.UserResponse;

@Service
public class UserServices {

	@Autowired
	private UsersRepository userRepo;

	@Autowired
	private PasswordService pwd;

	@Autowired
	private SessionRepository sessionRepo;

	@Autowired
	private TokenGenerator tokenService;

	@Autowired
	private OTPService otpService;

	@Autowired
	private RegistrationOTPService registerOtpService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsService smsService;
	
	@Autowired
	private VendorService vendorService;
	
	@Autowired
	private ProductRepository pRepo;
	
	@Autowired
	private CouponRepository cRepo;

	Logger logger = LoggerFactory.getLogger(UserServices.class);

	// Create a new user in the system
	public UserResponse createNewUser(UserDTO req) {

		UserResponse res = new UserResponse();
		if (req.getFullName() == null) {
			res.setValid(false);
			res.setMessage("Fullname is required");
		}

		else if (req.getEmail() == null) {
			res.setValid(false);
			res.setMessage("Email is required");
		}

		else if (req.getUserType() == null) {
			res.setValid(false);
			res.setMessage("Usertype is required");
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
			Users userToSave = new Users();
			userToSave.setCreated_on(LocalDate.now());
			BeanUtils.copyProperties(req, userToSave);
			if (req.getUserType().equalsIgnoreCase("admin")) {
				userToSave.setActive(true);
				userToSave.setUserType(userType.ADMIN);
			} else if (req.getUserType().equalsIgnoreCase("vendor")) {
				userToSave.setActive(true);
				userToSave.setUserType(userType.VENDOR);
			} else {
				userToSave.setActive(false);
				userToSave.setUserType(userType.USER);
			}

			userToSave.setPassword(pwd.encrypt(req.getPassword()));
			if (req.getGender().equalsIgnoreCase("female")) {
				userToSave.setGender("female");
			} else {
				userToSave.setGender("male");
			}

			if (req.getUserId() != null) {
				if (_isValidUser(req.getUserId())) {
					userToSave.setUserId(req.getUserId());
				} else {
					res.setValid(false);
					res.setMessage("User ID is invalid");
				}
			}

			try {
				// Check if existing user exists
				Users exists = userRepo.findByEmail(req.getEmail());
				if (exists != null && req.getUserId() == null) {
					res.setValid(false);
					res.setMessage("Email already exists");
				} else {
					
					// SAVE USER TO DB
					userRepo.save(userToSave);
							
					// Create Vendor User:
					if(userToSave.getUserType().equals(userType.VENDOR)) {
						Vendor vendor = new Vendor();
						vendor.setUser(userToSave);
						vendor.setVendorName(userToSave.getFullName());
						vendor.setCreated_on(LocalDate.now());
						vendorService.saveVendor(vendor);
					}
					
					Address address = new Address();
					BeanUtils.copyProperties(req, address);
					if (req.getCountry() == null) {
						address.setCountry("INDIA");
					}
					address.setCreated_on(LocalDate.now());
					address.setUser(userToSave);

					int otp = registerOtpService.generateOTP(userToSave.getEmail());

					// Send mail to user:
					boolean result = false;
					if (req.getUserType().equalsIgnoreCase("admin") || req.getUserType().equalsIgnoreCase("vendor") || req.getUserType().equalsIgnoreCase("delivery")) {
						result = true;
					} else {
						String text = smsService.forgetPassOTPMessage(otp);
						SMSModel sms = new SMSModel("+91" + req.getMobile(), "", text);
						smsService.sendSms(sms);
						result = emailService.sendRegistrationOtpMessage(userToSave.getEmail(), Integer.toString(otp),
								userToSave.getFullName());
					}

					if (!result) {
						res.setValid(false);
						res.setMessage("Unable to send email to " + userToSave.getEmail());
						userRepo.delete(userToSave);
						return res;
					}
					try {
						res.setValid(true);
						res.setMessage("User created successfully");
						BeanUtils.copyProperties(userToSave, res);
						BeanUtils.copyProperties(address, res);
					} catch (Exception e) {
						logger.error("Failed to run: " + e);
						res.setValid(false);
						res.setMessage("Address creation failed");
						return res;
					}

				}
			} catch (Exception e) {
				logger.error("Failed to run: " + e);
				res.setValid(false);
				res.setMessage("User creation failed");
				return res;
			}
		}

		return res;

	}

	public Users updateUser(UpdateUserDTO req) {
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
					Users user = userRepo.fetchByUserId(req.getUserId());
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

	public GeneralResponse activateUser(RegisterOTPValidation req) {

		GeneralResponse res = new GeneralResponse();
		if (req.getUserId() == null || req.getOtp() == null) {
			res.setMessage("Invalid Request");
		}

		try {
			Users user = userRepo.fetchByUserId(req.getUserId());
			if (user == null) {
				res.setMessage("User not found");
			}

			int otp = registerOtpService.getOtp(user.getEmail());

			if (otp == Integer.parseInt(req.getOtp())) {
				user.setActive(true);
				userRepo.save(user);
				res.setMessage("User is activated");
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

	public String checkUserExists(Long userId) {
		try {
			Users user = userRepo.fetchByUserId(userId);
			if (user == null) {
				return "Not Found";
			} else if (!user.isActive()) {
				return "User exists";
			} else {
				return "User already active";
			}
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return "Error";
		}
	}

	public List<Users> getAllUsers() {
		List<Users> users = userRepo.fetchByAdmin(userType.ADMIN.toString(), userType.VENDOR.toString());
		return _removePassword(users);
	}

	public List<Users> getAllActiveUsers() {
		List<Users> users = userRepo.findByIsActive(true);
		return _removePassword(users);
	}
	
	public List<Users> getVendorUsers() {
		List<Users> users = userRepo.findByUserType(userType.VENDOR);
		return _removePassword(users);
	}

	public Users findUserById(Long userId) {
		return userRepo.fetchByUserId(userId);
	}

	public Users findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public boolean triggerOTP(Long userId) {
		Users user = userRepo.getOne(userId);
		if (user == null || user.isActive()) {
			return false;
		}

		int otp = registerOtpService.generateOTP(user.getEmail());

		// Send mail to user:
		boolean result = false;
		try {
			String text = smsService.forgetPassOTPMessage(otp);
			SMSModel sms = new SMSModel("+91" + user.getMobile(), "", text);
			smsService.sendSms(sms);
			result = emailService.sendRegistrationOtpMessage(user.getEmail(), Integer.toString(otp),
					user.getFullName());
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return false;
		}
		return result;
	}

	public LoginResponse loginUser(LoginDTO req) {
		try {
			Users user = userRepo.findByEmail(req.getEmail());
			if (user == null) {
				return null;
			}

			String password = pwd.decrypt(user.getPassword());
			if (password.equals(req.getPassword())) {

				// If user is inactive, flag it out:
				if (!user.isActive()) {
					LoginResponse r = new LoginResponse();
					r.setMessage("PENDING");
					r.setUserId(user.getUserId());
					return r;
				}

				Session checkSession = sessionRepo.findByUserIdAndUserType(user.getUserId(), user.getUserType().toString());
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
	
	public LoginResponse loginVendorUser(LoginDTO req) {
		try {
			Users user = userRepo.findByEmail(req.getEmail());
			if (user == null) {
				return null;
			}

			String password = pwd.decrypt(user.getPassword());
			if (password.equals(req.getPassword()) && user.getUserType().equals(userType.VENDOR)) {

				// If user is inactive, flag it out:
				if (!user.isActive()) {
					LoginResponse r = new LoginResponse();
					r.setMessage("PENDING");
					r.setUserId(user.getUserId());
					return r;
				}

				Session checkSession = sessionRepo.findByUserIdAndUserType(user.getUserId(), user.getUserType().toString());
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
			Users user = userRepo.findByEmail(req.getEmail());
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
			Users user = userRepo.findByEmail(email);
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
			Users user = userRepo.fetchByUserId(userId);
			Session s = sessionRepo.findByUserIdAndUserType(userId, user.getUserType().toString());
			if (s != null) {
				sessionRepo.delete(s);
			}
			List<Product> productsToDelete = pRepo.fetchByVendorId(user.getUserId());
			for (Product product : productsToDelete) {
				pRepo.delete(product);
			}
			List<Coupon> couponsToDelete = cRepo.findByVendorId(userId);
			for(Coupon coupon : couponsToDelete) {
				cRepo.delete(coupon);
			}
			userRepo.delete(user);
			return true;
		} catch (Exception e) {
			logger.error("Failed deleting operation due to: " + e.getMessage());
			e.printStackTrace();
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
			Users user = userRepo.findByEmail(req.getEmail());
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

	public boolean logoutUser(Users user) {
		try {
			sessionRepo.deleteSession(user.getUserId(), user.getUserType().toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean _isValidUser(Long userId) {
		try {
			if (userRepo.fetchByUserId(userId) == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return false;
		}
	}

	private List<Users> _removePassword(List<Users> users) {
		for (Users u : users) {
			u.setPassword(null);
		}

		return users;
	}

	private LoginResponse generateUserSession(Users user) {

		// Check if already session exists
		Session checkSession = sessionRepo.findByUserIdAndUserType(user.getUserId(), user.getUserType().toString());
		if (checkSession != null) {
			sessionRepo.deleteSession(checkSession.getUserId(),checkSession.getUserType());
		}

		Session session = new Session();
		session.setUserId(user.getUserId());
		session.setUserType(user.getUserType().toString());
		session.setToken(tokenService.generateToken());
		session.setCreated_on(LocalDate.now());
		try {
			sessionRepo.save(session);
		} catch (Exception e) {
			logger.error("Unable to create session for user: " + user.getUserId());
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
		resp.setUserId(user.getUserId());
		resp.setUserType(user.getUserType());
		return resp;
	}

}
