package io.shiromi.saml.streams;

import io.shiromi.saml.annotations.SerializedItem;

import io.shiromi.saml.elements.YamlElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

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
}
