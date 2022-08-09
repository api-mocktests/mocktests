package org.api.mocktests.extensions;

import org.api.mocktests.annotations.Authenticate;
import org.api.mocktests.models.Request;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Component
public class AuthenticateExtension {

    public boolean fieldLoginIsIstantiated(Class<?> aClass) {

        try {
            for (Field field : aClass.getDeclaredFields()) {
                if(field.isAnnotationPresent(Authenticate.class))
                    return true;
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }

    public Request getFieldLogin(Class<?> aClass) {

        try {
            for (Field field : aClass.getDeclaredFields()) {

                if(field.isAnnotationPresent(Authenticate.class)) {
                    field.setAccessible(true);
                    return (Request) field.get(aClass.getConstructor().newInstance());
                }
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
