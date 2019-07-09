package com.work.shop.oms.express.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;

public class TestKuai100 {
	
	
	public static void main(String[] args) throws Exception {
		
		URI u = new URI("http://api.kuaidi100.com/api?id=47b7acf3bdba0d16&com=yunda&nu=1900508437277&show=0&muti=1&order=asc");
		java.net.URL url = u.toURL();
		URLConnection connection = url.openConnection();
		connection.connect();
		//System.out.println(new String(readStream(connection.getInputStream())));
	}
	
	 public static byte[] readStream(InputStream inStream) throws Exception {  
	        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = -1;  
	        while ((len = inStream.read(buffer)) != -1) {  
	            outSteam.write(buffer, 0, len);  
	        }  
	        outSteam.close();  
	        inStream.close();  
	        return outSteam.toByteArray();  
	    }  

}
