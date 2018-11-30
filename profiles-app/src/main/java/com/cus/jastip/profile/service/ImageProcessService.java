package com.cus.jastip.profile.service;

import java.io.IOException;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageProcessService {

	private static final String CLOUDINARY_CLOUD_NAME = "drdrxjaif";
	private static final String CLOUDINARY_API_KEY = "667642892298513";
	private static final String CLOUDINARY_API_SECRET = "xxCK7QupUAKGbYmeVrSqutp-gxU";
	Cloudinary cloudinary = getCloudinaryClient();

	public String onSubmit(byte[] bs) throws Exception {

		@SuppressWarnings("unused")
		String fileName = "";

		Map<String, Object> cloudinaryURL = null;

		if (bs != null) {
			try {
				fileName = bs.toString();
				cloudinaryURL = uploadToCloudinary(cloudinary, bs);
			} catch (Exception e) {
				return "fail";
			}

		}

		System.out.println("Clodinary URL: " + (String) cloudinaryURL.get("public_id"));

		// apply Transformation on the uploaded image and get the url from Cloudinary
		// cloud

		/*
		 * String url =
		 * cloudinary.url().format("jpg").generate("mm_images/profile/"+fileName.split(
		 * "\\.", 3)[0]);
		 */

		return bs.toString();

	}

	public String urlImage(String uri) {
		String url = cloudinary.url().format("jpg").transformation(new Transformation().radius("max"))
				.generate("mm_images/profile/" + uri.toString().split("\\.", 3)[0]);
		return url;
	}

	public String urlImageThumb(String uri) {
		String url = cloudinary.url().format("jpg")
				.transformation(new Transformation().width(150).height(150).gravity("face").radius("max"))
				.generate("mm_images/profile/" + uri.toString().split("\\.", 3)[0]);
		return url;
	}

	private static Cloudinary getCloudinaryClient() {

		return new Cloudinary(ObjectUtils.asMap("cloud_name", CLOUDINARY_CLOUD_NAME, "api_key", CLOUDINARY_API_KEY,
				"api_secret", CLOUDINARY_API_SECRET, "secure", true));
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Object> uploadToCloudinary(Cloudinary cloudinary, byte[] sourceFile) throws IOException {

		String filename = sourceFile.toString();
		Map<String, Object> cloudinaryUrl = null;
		Map params = ObjectUtils.asMap("public_id", "mm_images/profile/" + filename.split("\\.", 3)[0]);

		// Convert multipart file type image to File type because Cloudinary doesn't
		// accept multipart file type.

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().upload(sourceFile, params);
			cloudinaryUrl = result;

		} catch (IOException e) {
			System.out.println("Could not upload file to Cloundinary from Byte[] " + filename + e.toString());
			throw e;
		}

		return cloudinaryUrl;
	}

}
