package com.treggo.grocericaApi.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.requests.SMSModel;

@Service
public class SmsService {

	@Value("${app.sms.enabled}")
	private boolean isSmsEnabled;

	@Value("${app.sms.authtoken}")
	private String authToken;

	@Value("${app.sms.sentFrom}")
	private String sentFrom;

	Logger logger = LoggerFactory.getLogger(UserServices.class);

	public void sendSms(SMSModel sms) {
		if(!isSmsEnabled) {
			return;
		}
		try {
			// Construct data
			String apiKey = "apikey=" + URLEncoder.encode(authToken, "UTF-8");
			String message = "&message=" + URLEncoder.encode(sms.getMessage(), "UTF-8");
			String sender = "&sender=" + URLEncoder.encode(sentFrom, "UTF-8");
			String numbers = "&numbers=" + URLEncoder.encode("91" + sms.getTo(), "UTF-8");

			// Send data
			String data = "https://api.textlocal.in/send/?" + apiKey + numbers + message + sender;
			URL url = new URL(data);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			String sResult = "";
			while ((line = rd.readLine()) != null) {
				// Process line...
				sResult = sResult + line + " ";
			}
			rd.close();
			System.out.println("OTP SENT SUCCESSFULLY");
			logger.debug("OTP sent successfully to: " + sms.getTo());

		} catch (Exception e) {
			logger.error("Error while sending SMS: " + e);
			System.out.println("UNABLE TO SEND OTP");
		}
	}

	public String newUserOTPMessage(int otp) {
		return "Welcome, use this OTP to register: " + Integer.toString(otp);
	}

	public String forgetPassOTPMessage(int otp) {
		return "Welcome, use this OTP to recover account: " + Integer.toString(otp);
	}
}
