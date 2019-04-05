package com.ck.guavam1;

import com.google.common.base.Optional;

/**
 * Hello world!
 *
 */
public class App1 {
	public static void main(String[] args) {
		System.out.println(test());
	}

	public static String test() {
		try {
			Optional<String> s = Optional.fromJavaUtil(java.util.Optional.empty());
			return s.toString();
		} catch (Throwable t) {
			return t.toString();
		}
	}
}
