package org.api.mocktests.extensions;

import org.api.mocktests.annotations.AuthenticatedTest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticatedTestExtension {

    public AuthenticatedTestExtension() {
        super();
    }

    public List<String> getMethodsAuthenticatedTest(Class<?> aClass) {

        List<String> listMethods = new ArrayList<>();

        try {
            for(Method method : aClass.getDeclaredMethods()) {
                if(method.isAnnotationPresent(AuthenticatedTest.class)) {
                    listMethods.add(method.getName());
                }
            }
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }

        return listMethods;
    }

    public StackTraceElement getMethods() {
        return Thread.currentThread().getStackTrace()[5];
    }
}
