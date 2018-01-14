package com.workplace.employee.util;

public final class Validate {
	
	public static void notNull(Object obj) {
		if(obj == null) {
			throw new IllegalArgumentException( "Parameter cannot be null");
		}
	}

}
