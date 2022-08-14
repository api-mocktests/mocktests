package org.api.mocktests.extensions;

import org.api.mocktests.annotations.AutoConfigureRequest;
import org.api.mocktests.exceptions.InvalidRequestException;
import org.springframework.stereotype.Component;

@Component
public class AutoConfigureRequestExtension {

    public boolean classIsAnnotAutoConfigureContext(Class<?> aClass) {

        try {
            if(aClass.isAnnotationPresent(AutoConfigureRequest.class)) {
                return true;
            }
        } catch (SecurityException e) {
            return false;
        }
        return false;
    }

    public boolean classIsAnnotAutoConfigureHeader(Class<?> aClass) {

        try {
            if(aClass.isAnnotationPresent(AutoConfigureRequest.class)) {
                return true;
            }
        } catch (SecurityException securityException) {
            return false;
        }
        return false;
    }

    public String getAutoConfigureContextType(Class<?> aClass) throws InvalidRequestException {
        try {
            if(aClass.isAnnotationPresent(AutoConfigureRequest.class)) {
                return aClass.getAnnotation(AutoConfigureRequest.class).mediatype();
            }
        } catch (Exception e) {
            throw new InvalidRequestException("contextType not definite");
        }

        return null;
    }

    public String[] getAutoConfigureHeader(Class<?> aClass) throws InvalidRequestException {

        try {
            if(aClass.isAnnotationPresent(AutoConfigureRequest.class)) {
                return aClass.getAnnotation(AutoConfigureRequest.class).header();
            }
        } catch (Exception e) {
            throw new InvalidRequestException("header not definite");
        }

        return null;
    }
}
