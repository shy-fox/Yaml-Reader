package io.shiromi.saml.streams;

import io.shiromi.saml.elements.YamlElement;
import io.shiromi.saml.exceptions.DuplicateEntryException;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class YamlWriter {
//    @Contract(pure = true)
//    public static void write(@NotNull Object object, File file) throws IOException {
//        Class<?> classOf = object.getClass();
//
//        Field[] fields = classOf.getFields();
//
//        BufferedWriter r = new BufferedWriter(new FileWriter(file));
//
//        for (Field f : fields) {
//            Annotation[] as = f.getAnnotations();
//
//            String name = f.getName();
//
//            for (Annotation a : as) {
//                if (a instanceof SerializedItem s) name = s.name();
//                break;
//            }
//
//            YamlElement<?> e = YamlElement.fromField(f, object, name);
//            assert e != null;
//            r.write(e.toBuffer());
//        }
//
//        r.close();
//    }

    private String fileName;
    private String[] path;
    private YamlElement<?>[] content;

    public YamlWriter(File f) throws FileNotFoundException {
        if (!f.exists()) throw new FileNotFoundException(String.format("File %s does not exist on this system.", f));
        this.fileName = f.getName();
        this.path = f.getPath().split("/");

        this.content = new YamlElement[0];
    }

    // DEBUG
    public YamlWriter() {
        this.content = new YamlElement[0];
        this.fileName = "test.yaml";
        this.path = new String[]{ };
    }

    public YamlWriter(String path) throws FileNotFoundException {
        this(new File(path));
    }

    public final YamlWriter put(YamlElement<?> element) throws DuplicateEntryException {
        if (contains(this.content, element))
            throw new DuplicateEntryException(String.format("Element with name %s already exists.", element.getName()));

        YamlElement<?>[] tmp = new YamlElement[content.length + 1];
        System.arraycopy(content, 0, tmp, 0, content.length);
        tmp[tmp.length - 1] = element;
        content = tmp;

        return this;
    }

    @Override
    public String toString() {
        return "YamlWriter{" +
                "fileName='" + fileName + '\'' +
                ", path=" + Arrays.toString(path) +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    private static boolean contains(Object[] a, Object b) {
        for (Object c : a) if (Objects.equals(b, c)) return true;
        return false;
    }
}
