package org.api.mocktests.extensions;

import org.api.mocktests.annotations.AuthenticatedTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AuthenticatedTestExtension {

    private Object object;

    public AuthenticatedTestExtension(Object object) {
        super();
        this.object = object;
    }

    public List<String> getMethodsAuthenticatedTest(Object object) {

        List<String> listMethods = new ArrayList<>();

        try {
            Class<?> c = object.getClass();
            for(Method method : c.getDeclaredMethods()) {
                if(method.isAnnotationPresent(AuthenticatedTest.class)) {
                    listMethods.add(method.getName());
                }
            }
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }

        return listMethods;
    }
    public String[] getMethods() {

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String[] listMethodsName = new String[5];

        int index = 0;
        for(int i = 0; i < 5; i++) {
            String nameMethod = stackTraceElements[i].getMethodName();
            if(nameMethod.equals("invoke") || nameMethod.equals("execute")) {
                index++;
                i--;
            }
            else
                listMethodsName[i] = nameMethod;

        }

        return listMethodsName;
    }
}
