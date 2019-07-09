package com.work.shop.test;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import com.work.shop.oms.express.utils.ExpressConstant;


public class kuaidi100
{
	
	public static void main(String[] agrs)
	{
		
		try
		{
			String key = ExpressConstant.KUAIDI100_KEY[new Random().nextLong() % 2 == 0 ? 0 : 1];
			System.out.println(key);
			String company = "zhongtong";
			String trackNo = "414502373869";
			String queryUrl = "http://api.kuaidi100.com/api?id=" + key + "&com=" + company + "&nu=" + trackNo + "&show=0&muti=1&order=asc";
			URL url= new URL(queryUrl);
			URLConnection con=url.openConnection();
			 con.setAllowUserInteraction(false);
			   InputStream urlStream = url.openStream();
			   String type = con.guessContentTypeFromStream(urlStream);
			   String charSet=null;
			   if (type == null)
			    type = con.getContentType();

			   if (type == null || type.trim().length() == 0 || type.trim().indexOf("text/html") < 0)
			    return ;

			   if(type.indexOf("charset=") > 0)
			    charSet = type.substring(type.indexOf("charset=") + 8);

			   byte b[] = new byte[10000];
			   int numRead = urlStream.read(b);
			  String content = new String(b, 0, numRead);
			   while (numRead != -1) {
			    numRead = urlStream.read(b);
			    if (numRead != -1) {
			     //String newContent = new String(b, 0, numRead);
			     String newContent = new String(b, 0, numRead, charSet);
			     content += newContent;
			    }
			   }
			   System.out.println("content:" + content);
			   urlStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
