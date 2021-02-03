package com.treggo.grocericaApi.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.treggo.grocericaApi.entities.ImgMaster;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.services.ImageService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/images")
public class ImageController {

	@Autowired
	private ImageService imgService;

	@Autowired
	private TokenGenerator tokenService;
	
	Logger logger = LoggerFactory.getLogger(ImageController.class);

	@ApiOperation(value = "Upload image to server")
	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
			@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		ImgMaster imgMaster = new ImgMaster();
		imgMaster.setImgPath(file.getOriginalFilename());
		imgMaster.setFileExtension(file.getContentType());
		String base64String;
		try {
			base64String = Base64.getEncoder().encodeToString(file.getBytes());
			imgMaster.setImgData(base64String);
			imgService.saveImage(imgMaster);
		}

		catch (Exception e) {
			logger.error("Failed to run: " + e);
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		}

		return ResponseEntity.ok(imgMaster);
	}

	@ApiOperation(value = "Download image from server based on image ID")
	@GetMapping("/download/{imgId}")
	public ResponseEntity<?> downloadImage(@PathVariable Long imgId) {

		ImgMaster img = imgService.getImage(imgId);

		if (img == null) {
			return ResponseEntity.status(404).body(new GeneralResponse("not found"));
		}

		byte[] byteData;
		try {
			byteData = Base64.getDecoder().decode(new String(img.getImgData()).getBytes("UTF-8"));
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(byteData);
		}

		catch (UnsupportedEncodingException e) {
			logger.error("Failed to run: " + e);
			return ResponseEntity.status(404).body(new GeneralResponse("failure"));
		}

	}

}
