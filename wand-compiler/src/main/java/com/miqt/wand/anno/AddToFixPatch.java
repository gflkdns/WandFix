package com.miqt.wand.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * the class file using this annotation will be packaged into the hot fix pudding
 */
@Documented
@Retention(SOURCE)
@Target({ElementType.TYPE})
public @interface AddToFixPatch {
}
