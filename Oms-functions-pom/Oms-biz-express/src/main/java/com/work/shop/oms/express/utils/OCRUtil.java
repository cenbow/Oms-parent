package com.work.shop.oms.express.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OCRUtil {

	private static final Log logger = LogFactory.getLog(OCRUtil.class);
	private static final String LANG_OPTION = "-l";
	// private static final String EOL = File.separator;
	private static final String IMAGE_FORMAT = "jpg";
	private static String tessPath = "";

	public static String recognizeValidation(InputStream in) throws Exception {
		logger.debug("input IMG InputStream : " + in);
		if (in == null)
			return "";
		File tmpFile = File.createTempFile("img", "." + IMAGE_FORMAT);
		OutputStream out = new FileOutputStream(tmpFile);
		IOUtils.copy(in, out);
		IOUtils.closeQuietly(out);
		return format(recognizeText(tmpFile, IMAGE_FORMAT));
	}

	private static String format(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		StringBuffer sb = new StringBuffer(str.length());
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c) || Character.isLetter(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String recognizeText(File imageFile, String imageFormat)
			throws Exception {
		// File tempImage = createImage(imageFile, imageFormat);
		File tempImage = imageFile;
		File outputFile = new File(imageFile.getParentFile(), "output");
		StringBuffer strB = new StringBuffer();

		logger.debug("out file:" + outputFile.getAbsoluteFile());
		try {
			String OS = System.getenv("OS").toLowerCase();
			if (OS.contains("windows")) {
				tessPath = "C:\\Tesseract\\";
			}
			logger.debug("os:" + OS);
		} catch (Exception e) {
			logger.debug(" linux system");
		}
		String cmd_linux = tessPath + "tesseract "
				+ tempImage.getAbsoluteFile() + " "
				+ outputFile.getAbsoluteFile() + " -l eng";
		logger.debug("doing cmd :" + cmd_linux + " imagePath:"
				+ tempImage.getAbsolutePath());
		Process process = Runtime.getRuntime().exec(cmd_linux);
		int w = process.waitFor();
		tempImage.delete();
		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
					"UTF-8"));
			String str;
			while ((str = in.readLine()) != null) {
				strB.append(str);// .append(EOL);
			}
			in.close();
		} else {
			String msg = "";
			switch (w) {
			case 1:
				msg = "Errors accessing files. There may be spaces in your image's filename.";
				break;
			case 29:
				msg = "Cannot recognize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
			}
			return msg;
		}
		// tempImage.delete();
		logger.debug("checkCode:" + strB.toString());
		// //System.out.println(strB.toString());
		return strB.toString();
	}

	private static File tempImageFile(File imageFile) {
		String path = imageFile.getPath();
		StringBuffer strB = new StringBuffer(path);
		strB.insert(path.lastIndexOf('.'), 0);
		return new File(strB.toString().replaceFirst("(?<=\\.)(\\w+)$", "tif"));
	}

	public static void main(String[] args) throws Exception {
		// String maybe = recognizeValidation(new
		// URL("http://www.ems.com.cn/ems/rand?0.83626081742").openStream());
		// String maybe2 = new OCRUtil().recognizeText(new File("f:/rand.jpeg"),
		// "jpeg");
		File ss = new File("d:\\", "output");
		//System.out.println(System.getenv("OS"));
	}
}
