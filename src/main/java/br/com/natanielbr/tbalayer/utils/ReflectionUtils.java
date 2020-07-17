package br.com.natanielbr.tbalayer.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ReflectionUtils {
	public static Method findMethod(Class<?> classe, String methodName) {
		return findMethod(classe, (a)-> a.getName().equalsIgnoreCase(methodName));
	}
	public static Method findMethod(Class<?> classe, Predicate<Method> condition){
		return Arrays.stream(classe.getDeclaredMethods()).filter(condition).findFirst().orElse(null);
	}
}
