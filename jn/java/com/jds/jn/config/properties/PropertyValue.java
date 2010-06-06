package com.jds.jn.config.properties;

import java.lang.annotation.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:10:29/10.04.2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface PropertyValue
{
	String value();
}
