package com.work.shop.oms.utils;

import java.util.Random;

public class GotCodeUtil {
	//可以将字符转换赋值给int类型，查看其ASCII码
	public static void main(String[] args) {
		//随机生成纯数字
		for(int i=0;i<15;i++)
			createData(8);
		System.out.println("---------------");
						   
		//生成数字字母
		for(int i=0;i<15;i++)
		createRandomCharData(8);
	}
	//根据指定长度生成字母和数字的随机数
	//0~9的ASCII为48~57
	//A~Z的ASCII为65~90
	//a~z的ASCII为97~122
	public static void createRandomCharData(int length)
	{
		StringBuilder sb=new StringBuilder();
		Random rand=new Random();//随机用以下三个随机生成器
		Random randdata=new Random();
		int data=0;
		for(int i=0;i<length;i++)
		{
			int index=rand.nextInt(3);
			//目的是随机选择生成数字，大小写字母
			switch(index)
			{
			case 0:
				 data=randdata.nextInt(10);//仅仅会生成0~9
				 sb.append(data);
				break;
			case 1:
				data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
				sb.append((char)data);
				break;
			case 2:
				data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
				sb.append((char)data);
				break;
			}
		}
		String result=sb.toString();
		System.out.println(result);
	}
					   
	//根据指定长度生成纯数字的随机数
	public static String createData(int length) {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(rand.nextInt(10));
		}
		String data = sb.toString();
		return data;
	}
}
