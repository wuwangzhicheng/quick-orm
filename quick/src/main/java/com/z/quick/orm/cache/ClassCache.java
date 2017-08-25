package com.z.quick.orm.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.z.quick.orm.annotation.Condition;
import com.z.quick.orm.annotation.Exclude;
import com.z.quick.orm.annotation.NoFind;
import com.z.quick.orm.annotation.PrimaryKey;
import com.z.quick.orm.annotation.Table;
import com.z.quick.orm.sql.builder.ISqlBuilder;
public class ClassCache {
	
	private static final Map<Class<?>,Map<String,Field>> allFieldClassCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> declaredFieldsCache = new HashMap<>();
	private static final Map<Class<?>,String> annationTableNameCache = new HashMap<>();
	private static final Map<Class<?>,String> annationSelectCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> annationPKCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> annationConditionCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> insertParamCache = new HashMap<>();
	
	public static Map<String,Field> getAllFieldMap(Class<?> clzz){
		Map<String,Field> fieldMap = allFieldClassCache.get(clzz);
		if (fieldMap != null) {
			return fieldMap;
		}
		fieldMap = parseClass(clzz);
		allFieldClassCache.put(clzz,fieldMap);
		return fieldMap;
	}
	
	public static List<Field> getDeclaredFields(Class<?> clzz){
		if (declaredFieldsCache.get(clzz) != null) {
			return declaredFieldsCache.get(clzz);
		}
		Field[] fields = clzz.getDeclaredFields();
		List<Field> list = new ArrayList<>(Arrays.asList(fields));
		declaredFieldsCache.put(clzz, list);
		return list;
	}
	public static Field getField(Class<?> clzz,String fieldName){
		return getAllFieldMap(clzz).get(fieldName);
	}
	public static String getTableName(Class<?> clzz){
		//TODO scham tableName判断
		if (annationTableNameCache.get(clzz) != null) {
			return annationTableNameCache.get(clzz);
		}
		Table table = clzz.getAnnotation(Table.class);
		if (table != null) {
			return table.tableName();
		}
		String tableName = clzz.getSimpleName();
		annationTableNameCache.put(clzz, tableName);
		return tableName;
		
	}
	public static String getSelect(Class<?> clzz){
		if (annationSelectCache.get(clzz) != null) {
			return annationSelectCache.get(clzz);
		}
		Field[] fields =  clzz.getDeclaredFields();
		List<Field> fieldList = new ArrayList<Field>(Arrays.asList(fields));
		fieldList.removeIf(f -> f.getAnnotation(Exclude.class)!=null); 
		fieldList.removeIf(f -> f.getAnnotation(NoFind.class)!=null); 
		StringBuffer selectsb = new StringBuffer();
		fieldList.forEach((f) -> {
			selectsb.append(ISqlBuilder.SPACE).append(f.getName()).append(",");
		});
		if (selectsb.length() == 0) {
			annationSelectCache.put(clzz, "");
			return "";
		}
		String select = selectsb.deleteCharAt(selectsb.lastIndexOf(",")).toString();
		annationSelectCache.put(clzz, select);
		return select;
		
	}
	
	public static List<Field> getPrimaryKey(Class<?> clzz){
		if (annationPKCache.get(clzz) != null) {
			return annationPKCache.get(clzz);
		}
		Field[] fields =  clzz.getDeclaredFields();
		List<Field> fieldList = new ArrayList<Field>(Arrays.asList(fields));
		fieldList.removeIf(f -> f.getAnnotation(PrimaryKey.class)==null); 
		annationPKCache.put(clzz, fieldList);
		return fieldList;
	}
	
	public static List<Field> getCondition(Class<?> clzz){
		if (annationConditionCache.get(clzz) != null) {
			return annationConditionCache.get(clzz);
		}
		Field[] fields =  clzz.getDeclaredFields();
		List<Field> fieldList = new ArrayList<Field>(Arrays.asList(fields));
		fieldList.removeIf(f -> f.getAnnotation(Condition.class)==null); 
		annationConditionCache.put(clzz, fieldList);
		return fieldList;
	}
	public static List<Field> getInsert(Class<?> clzz){
		if (insertParamCache.get(clzz) != null) {
			return insertParamCache.get(clzz);
		}
		Field[] fields =  clzz.getDeclaredFields();
		List<Field> fieldList = new ArrayList<Field>(Arrays.asList(fields));
		fieldList.removeIf(f -> f.getAnnotation(Exclude.class)!=null); 
		insertParamCache.put(clzz, fieldList);
		return fieldList;
	}
	
	
	private static Map<String,Field> parseClass(Class<?> clzz){
		Map<String,Field> fieldMap = new HashMap<>();
		Field[] fields = clzz.getDeclaredFields();
		for (Field field : fields) {
			fieldMap.put(field.getName(), field);
		}
		if (clzz.getSuperclass() != null) {
			fieldMap.putAll(parseClass(clzz.getSuperclass()));
		}
		return fieldMap;
	}
	
	
}
