package com.portfolio.base.common;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

@Component
public class ReflectionManager {

	public void invokeSetter(Object obj, String propertyName, Object propertyValue) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyDescriptor pd;
		
		pd=new PropertyDescriptor(propertyName, obj.getClass());
		Method setter= pd.getWriteMethod();
		
		setter.invoke(obj, propertyValue);
	}
	
	public void invokeGetter(Object obj, String propertyName, Object propertyValue) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyDescriptor pd;
		
		pd=new PropertyDescriptor(propertyName, obj.getClass());
		Method getter= pd.getReadMethod();
		
		getter.invoke(obj, propertyValue);
	}
}
