package com.miqt.wand.anno;


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
