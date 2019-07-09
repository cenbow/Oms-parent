package com.work.shop.oms.utils;

import java.sql.Types;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class ByteToTinyIntJavaTypeResolver extends JavaTypeResolverDefaultImpl{

	@Override
	public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
		int jdbctype = introspectedColumn.getJdbcType();
		if(jdbctype == Types.BIT) {
			System.out.println("bit");
			return new FullyQualifiedJavaType(Integer.class.getName());
		}
		return super.calculateJavaType(introspectedColumn);
	}
	

	@Override
	public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
		String jdbcTypeName = super.calculateJdbcTypeName(introspectedColumn);
		if(jdbcTypeName.equals("BIT")) {
			jdbcTypeName = "INTEGER";
		}
		return jdbcTypeName;
	}

	

}
