package com.ck.guavam2;

import com.google.common.collect.MapConstraints;

/**
 * Hello world!
 *
 */
public class App2 {
	public static void main(String[] args) {
		System.out.println(test());
	}

	public static String test() {
		try {
			return MapConstraints.notNull().toString();
		} catch (Throwable t) {
			return t.toString();
		}
	}
}
