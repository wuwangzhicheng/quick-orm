package com.z.quick.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ******************  类说明  *********************
 * class       :  Table
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ***********************************************
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	String tableName();
}
