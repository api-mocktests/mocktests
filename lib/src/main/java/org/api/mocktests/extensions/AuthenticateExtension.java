package org.api.mocktests.extensions;

import org.api.mocktests.annotations.Authenticate;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AuthenticateExtension {

    public boolean methodLoginIsIstantiated(Class<?> aClass) {

        try {

            //Class<?> c = object.getClass();
            for (Method method : aClass.getDeclaredMethods()) {

                if(method.isAnnotationPresent(Authenticate.class))
                    return true;
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }

    public ResultActions invokeMethodLogin(Class<?> aClass) {

        try {
            //Class<?> c = object.getClass();
            //Object object = aClass.newInstance();
            //System.out.println(object);
            //Constructor<?> constructor = (Constructor<?>) aClass.getConstructor().newInstance();
            for (Method method : aClass.getDeclaredMethods()) {

                if(method.isAnnotationPresent(Authenticate.class)) {
                    method.setAccessible(true);
                    return (ResultActions) method.invoke(aClass.getConstructor().newInstance());
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
