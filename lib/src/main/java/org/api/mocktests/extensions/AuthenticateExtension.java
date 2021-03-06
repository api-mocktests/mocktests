package org.api.mocktests.extensions;

import org.api.mocktests.annotations.Authenticate;
import org.api.mocktests.annotations.AuthenticatedTest;
import org.api.mocktests.models.Request;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AuthenticateExtension {

    public static void verifyMethodLoginImplement(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            for (Method m : clazz.getDeclaredMethods()) {
                if(m.isAnnotationPresent(Authenticate.class)) {
                    m.setAccessible(true);
                    System.out.println(m.invoke(obj));
                    System.out.println(m);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean fieldLoginIsIstantiated(Object object) {

        try {

            Class<?> c = object.getClass();
            for (Field field : c.getDeclaredFields()) {

                if(field.isAnnotationPresent(Authenticate.class))
                    return true;
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }

    public Request getFieldLogin(Object object) {

        try {

            Class<?> c = object.getClass();
            for(Field field : c.getDeclaredFields()) {

                if(field.isAnnotationPresent(Authenticate.class)) {
                    field.setAccessible(true);
                    return (Request) field.get(object);
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println("erro ao converter");
        }
        return null;
    }

    public ResultActions invokeMethodLogin(Object object) {

        try {

            Class<?> c = object.getClass();
            System.out.println(c.getName());
            for (Method method : c.getDeclaredMethods()) {

                if(method.isAnnotationPresent(Authenticate.class)) {
                    method.setAccessible(true);
                    method.invoke(object);
                    System.out.println("invoke0");
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void verifyTestsUsingAnnotation(Object obj) {

        try {
            Class clazz = obj.getClass();
            for(Method m : clazz.getDeclaredMethods()) {
                if(m.isAnnotationPresent(AuthenticatedTest.class)) {
                    m.setAccessible(true);
                    System.out.println(m);
                    System.out.println(m.invoke(obj));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
