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

        for(int i = 0; i < 5; i++) {
            listMethodsName[i] = stackTraceElements[i].getMethodName();
            Class<?> cla = stackTraceElements[i].getClass();
            System.out.println(cla.getName());
        }
        System.out.printf("[%s, %s, %s, %s, %s]%n",listMethodsName[0], listMethodsName[1], listMethodsName[2], listMethodsName[3], listMethodsName[4]);
        return listMethodsName;
    }
}
