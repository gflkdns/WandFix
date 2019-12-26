package wandfix.test;

import dalvik.system.DexClassLoader;

public class MyDexLoader extends DexClassLoader {
    public MyDexLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            try {
                c = findClass(name);
            } catch (ClassNotFoundException e) {

            }
        }
        if (c == null) {
            try {
                if (getParent() != null) {
                    c = getParent().loadClass(name);
                }
            } catch (ClassNotFoundException e) {
            }
        }
        if (c == null) {
            throw new ClassNotFoundException(name);
        }
        return c;
    }
}
