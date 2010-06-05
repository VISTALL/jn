package com.jds.jn.properties;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:38:54/10.04.2010
 */
public class ObjectParse
{
	public static Object parse(Class<?> clazz, String val)
	{
		val = val.trim();

		if (clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(Byte.TYPE))
		{
			return Byte.parseByte(val);
		}

		if (clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(Short.TYPE))
		{
			return Short.parseShort(val);
		}

		if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(Integer.TYPE))
		{
			return Integer.parseInt(val);
		}

		if (clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(Long.TYPE))
		{
			return Long.parseLong(val);
		}

		if (clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(Float.TYPE))
		{
			return Float.parseFloat(val);
		}

		if (clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(Double.TYPE))
		{
			return Double.parseDouble(val);
		}

		if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(Boolean.TYPE))
		{
			return Boolean.parseBoolean(val);
		}

		if (clazz.isAssignableFrom(String.class))
		{
			return String.valueOf(val);
		}

		if (clazz.isEnum())
		{
			for (Object o : clazz.getEnumConstants())
			{
				Enum<?> enu = (Enum<?>) o;
				if (enu.name().equals(val))
				{
					return enu;
				}
			}

			System.out.println("Not found value for enum: " + clazz.getSimpleName() + "; value: " + val);
			return null;
		}

		if (clazz.isAssignableFrom(Object.class))
		{
			return val;
		}

		return null;
	}
}
