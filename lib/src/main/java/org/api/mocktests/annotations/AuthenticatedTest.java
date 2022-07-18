package org.api.mocktests.annotations;

import org.apiguardian.api.API;
import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(
        status = API.Status.EXPERIMENTAL
)
@Testable
public @interface AuthenticatedTest {
}
