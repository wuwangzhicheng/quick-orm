package com.z.quick.orm.sql.builder;

import com.z.quick.orm.sql.SqlInfo;

/**
 * ******************  类说明  *********************
 * class       :  ISqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  sql生成器接口
 * @see        :                        
 * ***********************************************
 */
public interface ISqlBuilder {
	enum SBType{
		SAVE,
		UPDATE,
		GET,
		FIND,
		PAGE_COUNT,
		PAGE_LIST
	}
	SqlInfo builderSql(Object o);
	
}
