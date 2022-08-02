package org.api.mocktests.extensions;

import org.api.mocktests.annotations.AutoConfigureRequest;
import org.api.mocktests.exceptions.InvalidRequestException;

public class AutoConfigureRequestExtension {

    public boolean classIsAnnotAutoConfigureContext(Object object) {

        try {
            Class<?> c = object.getClass();
            if(c.isAnnotationPresent(AutoConfigureRequest.class)) {
                return true;
            }
        } catch (SecurityException e) {
            return false;
        }
        return false;
    }

    public boolean classIsAnnotAutoConfigureHeader(Object object) {

        try {
            Class<?> c = object.getClass();
            if(c.isAnnotationPresent(AutoConfigureRequest.class)) {
                return true;
            }
        } catch (SecurityException securityException) {
            return false;
        }
        return false;
    }

    public String getAutoConfigureContextType(Object object) throws InvalidRequestException {

        try {
            Class<?> c = object.getClass();
            if(c.isAnnotationPresent(AutoConfigureRequest.class)) {
                return c.getAnnotation(AutoConfigureRequest.class).mediatype();
            }
        } catch (Exception e) {
            throw new InvalidRequestException("contextType not definite");
        }

        return null;
    }

    public String[] getAutoConfigureHeader(Object object) throws InvalidRequestException {

        try {
            Class<?> c = object.getClass();
            if(c.isAnnotationPresent(AutoConfigureRequest.class)) {
                return c.getAnnotation(AutoConfigureRequest.class).header();
            }
        } catch (Exception e) {
            throw new InvalidRequestException("header not definite");
        }

        return null;
    }
}
