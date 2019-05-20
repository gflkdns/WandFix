package com.miqt.wand.utils;

import android.text.TextUtils;

import com.miqt.wand.Wand;

public class R2 {
    /**
     * 为什么要用这个方法?
     * <br><br/>
     * 主要用于解决主module模块R文件是用final修饰的，
     * 而用final修饰的对象在java中视为常量，在编辑成.class文件的时候，这个常量
     * 不会使用R.id.***的方式去引用而是直接是数字。因此这个值因为我们每次编译造成同一个id实际的id数字不一致，导致
     * findviewid找出来的view为空或者类型异常。
     * <p>
     * 至于不在主module模块引用的R文件，则无须使用这个方法,直接正常填写id即可
     * <br><br/>
     * 这个方法就是为了解决这个问题
     * <br><br/>
     * <p>
     * 方便起见，可以使用正则表达式批量替换代码
     * <br><br/>
     * 使用： <b>([^\(\),]*R\.[^\(\),]*)
     * <br><br/>
     * 替换：<b>R2.id_("$1")<b/>
     *
     * @param id 填“R.id.idname”格式一定要正确，最好是先用android的findviewbyid然后再加上双引号就好了
     */
    public static int id_(String id) {
        String[] idElements = id.split("\\.");
        if (idElements.length < 3) {
            return -1;
        }
        String idname = idElements[idElements.length - 1];
        String idtype = idElements[idElements.length - 2];
        String R = idElements[idElements.length - 3];
        StringBuilder packagenameBuilder = new StringBuilder();
        if (idElements.length > 3) {
            for (int i = 0; i < idElements.length - 4; i++) {
                packagenameBuilder.append(idElements[i]).append(".");
            }
        }
        String packagename = packagenameBuilder.toString();
        if (TextUtils.isEmpty(packagename)) {
            packagename = Wand.get().getContext().getPackageName();
        }
        return Wand.get().getContext().getResources().getIdentifier(idname, idtype, packagename);
    }
}
