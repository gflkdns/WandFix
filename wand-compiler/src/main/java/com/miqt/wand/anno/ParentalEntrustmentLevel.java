package com.miqt.wand.anno;

/**
 * Error:Execution failed for task ':app:compileDebugJavaWithJavac'.
 * > java.lang.IllegalArgumentException: expected name but was NEVER
 */

public enum ParentalEntrustmentLevel {
    /**
     * 禁用双亲委托
     */
    NEVER,
    /**
     * 仅本项目依赖的类库启用双亲委托
     */
    PROJECT,
    /**
     * 仅android java 类库启用双亲委托
     */
    @Deprecated
    ANDROID,
    /**
     * 仅 java 类库启用双亲委托
     */
    @Deprecated
    JAVA
}
