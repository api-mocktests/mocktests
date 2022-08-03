package org.api.mocktests.extensions;

import org.api.mocktests.annotations.AuthenticatedTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AuthenticatedTestExtension {

    public AuthenticatedTestExtension() {
        super();
    }

    public List<String> getMethodsAuthenticatedTest(Class className) {

        List<String> listMethods = new ArrayList<>();

        try {
            //Class<?> c = object.getClass();
            Class<?> c = className;
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
    public StackTraceElement getMethods() {

        return Thread.currentThread().getStackTrace()[4];
        //StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        String[] listMethodsName = new String[5];
//
//        for(int i = 0; i < 5; i++) {
//            listMethodsName[i] = stackTraceElements[i].getMethodName();
//        }
//        return listMethodsName;
    }

    public Class<?> getClass(StackTraceElement ste) {
        String className = ste.getClassName();
        try {
            return ClassLoader.getPlatformClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
