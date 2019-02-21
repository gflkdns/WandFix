package com.miqt.wand.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * the class file using this annotation will be packaged into the hot fix pudding
 */
@Target({ElementType.TYPE})
public @interface AddToFixPatch {
}
