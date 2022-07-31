package org.api.mocktests.extensions;

import org.api.mocktests.annotations.AutoConfigureContextType;
import org.api.mocktests.exceptions.InvalidRequestException;

public class AutoConfigureContextTypeExtension {

    public boolean classIsAnnotAutoConfigureContext(Object object) {

        try {
            Class<?> c = object.getClass();
            if(c.isAnnotationPresent(AutoConfigureContextType.class)) {
                return true;
            }
        } catch (SecurityException e) {
            return false;
        }
        return false;
    }

    public String getAutoConfigureContextType(Object object) throws InvalidRequestException {

        try {
            Class<?> c = object.getClass();
            if(c.isAnnotationPresent(AutoConfigureContextType.class)) {
                return c.getAnnotation(AutoConfigureContextType.class).mediatype();
            }
        } catch (Exception e) {
            throw new InvalidRequestException("contextType not definite");
        }

        return null;
    }
}
