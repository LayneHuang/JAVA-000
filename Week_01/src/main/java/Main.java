import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        try {
            Class<?> myClass = new MyClassLoader().findClass("Hello");
            Method method = myClass.getMethod("hello");
            Object obj = myClass.newInstance();
            method.invoke(obj);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static class MyClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            String filePath = this.getClass().getResource("/._Hello.xlass").getPath();
            System.out.println(filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                throw new ClassNotFoundException(name);
            }
            byte[] bytes = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int size = fileInputStream.read(bytes);
                if (size == -1) {
                    throw new ClassNotFoundException(name);
                }
//                System.out.println("file size :" + size);
                for (int i = 0; i < bytes.length; ++i) {
                    bytes[i] = (byte) (255 - bytes[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
