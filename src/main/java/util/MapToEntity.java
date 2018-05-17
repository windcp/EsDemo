package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MapToEntity {

	private Map<String, Object> map;

	/**
	 * 将map传给实体类
	 * 
	 * @param map
	 * @param clazz
	 * @return
	 */
	public <T> T mapToEntity(Map<String, Object> map, Class clazz) {
		Constructor<T>[] constructor = clazz.getConstructors();
		T t = null;
		try {
			t = constructor[0].newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (t == null) {
			try {
				t = (T) clazz.getDeclaredConstructor().newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Field[] fields = t.getClass().getDeclaredFields();
		Method[] methods = clazz.getMethods();
		for (int i=0; i<fields.length; i++) {
			Object obj = map.get(camelToUnderline(fields[i].getName()));
			if (obj != null) {
				String type = fields[i].getGenericType().getTypeName() ;
				fields[i].setAccessible(true);
				try {
					if (type.endsWith("Date")) {
						if (obj instanceof Date) {
							fields[i].set(t, obj);
						} else {
							try {
								String value = String.valueOf(obj).replace("T", " ");
								SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								fields[i].set(t, simple.parse(value));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else if (type == "long" || type.endsWith("Long")) {
						fields[i].setLong(t, Long.parseLong(String.valueOf(obj)));
					} else if (type == "double" || type.endsWith("Double")) {
						fields[i].setDouble(t, Double.parseDouble(String.valueOf(obj)));
					} else if (type == "short" || type.endsWith("Short")) {
						fields[i].setShort(t, Short.parseShort(String.valueOf(obj)));			
					} else if (type == "byte" || type.endsWith("Byte")) {
						fields[i].setByte(t, Byte.parseByte(String.valueOf(obj)));
					} else if (type == "int" || type.endsWith("Integer")) {
						fields[i].setInt(t, Integer.parseInt(String.valueOf(obj)));
					} else if (type == "float" || type.endsWith("Float")) {
						fields[i].setFloat(t, Float.parseFloat(String.valueOf(obj)));
					} else if (type.endsWith("String")) {
						fields[i].set(t, obj);
					} else {
						//实体类中的属性是另一个实体类
						if (obj instanceof Map && fields[i].getGenericType().toString().startsWith("class")) {
							try {
								fields[i].set(t, mapToEntity((Map)obj, Class.forName(type)));
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//实体类中套集合
						else if (obj instanceof List) {
							System.out.println("======List=====" + type);
							if (type.startsWith("java.util.List")) {
								if (type.indexOf("<") != -1) {
									try {
										List list = (List) obj;
										//List result = Lists.newArrayList();
                                        List result = new ArrayList();
										for (int j=0; j<list.size(); j++) {
											if (list.get(j) instanceof Map) {
												Map map1 = (Map) (list.get(j));
												result.add(mapToEntity(map1, Class.forName(type.substring(type.indexOf("<") + 1, type.indexOf(">")))));
											}
										}
										fields[i].set(t, result);
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {
									fields[i].set(t, obj);
								}
							}
						}
						
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		return t;
	}

	/**
	 * 驼峰转下划线
	 * 
	 * @param camelName
	 * @return
	 */
	public String camelToUnderline(String camelName) {
		if (camelName != null && !"".equals(camelName)) {
			String newCamelName = String.valueOf(camelName.charAt(0)).toUpperCase() + camelName.substring(1);
			StringBuffer str = new StringBuffer();
			Matcher matcher = Pattern.compile("[A-Z]").matcher(newCamelName);
			while (matcher.find()) {
				String name = matcher.group();
				matcher.appendReplacement(str, "_" + name.toLowerCase());
			}
			matcher.appendTail(str);
			if (str.charAt(0) == '_') {
				str.delete(0, 1);
			}
			System.out.println("=============================驼峰：" + camelName + "\t 下划线:" + str.toString()
					+ "========================");
			return str.toString();
		}
		return null;
	}
	
	/**
	 * 下划线转驼峰
	 * 
	 * @param lineName
	 * @return
	 */
	public String lineToCamel(String lineName) {
		if (lineName != null || !"".equals(lineName)) {
			lineName = lineName.toLowerCase();
			String[] names = lineName.split("_");
			StringBuffer sb = new StringBuffer();
			for(String str: names) {
				sb.append(String.valueOf(str.charAt(0)).toUpperCase());
				sb.append(str.substring(1));
			}
			return String.valueOf(sb.toString().charAt(0)).toLowerCase() + sb.toString().substring(1);
		}
		return null;
	}

}
