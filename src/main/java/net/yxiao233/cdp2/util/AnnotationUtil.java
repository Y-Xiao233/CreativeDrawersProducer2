package net.yxiao233.cdp2.util;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class AnnotationUtil {
    private static final Map<Class<? extends Annotation>, List<Field>> annotationFieldsMap = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Annotation>, List<Class<?>>> annotationClassesMap = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Annotation>, List<Method>> annotationMethodsMap = new ConcurrentHashMap<>();
    public static boolean isAnnotationPresent(Class<? extends Annotation> annotationClass, Object obj){
        return obj.getClass().isAnnotationPresent(annotationClass);
    }

    public static List<Class<?>> getAllClasses(Class<? extends Annotation> annotationClass){
        return getAll(annotationClass,annotationClassesMap,(annotationData, list) -> {
            try {
                list.add(Class.forName(annotationData.memberName()));
            } catch (ClassNotFoundException e) {
                CreativeDrawersProducer2.LOGGER.error("",e);
            }
        });
    }

    public static List<Field> getAllFields(Class<? extends Annotation> annotationClass){
        return getAll(annotationClass,annotationFieldsMap,(annotationData, list) -> {
            try {
                Arrays.stream(Class.forName(annotationData.clazz().getClassName()).getDeclaredFields()).filter(field -> {
                    return field.getName().equalsIgnoreCase(annotationData.memberName());
                }).forEach(list::add);
            } catch (ClassNotFoundException e) {
                CreativeDrawersProducer2.LOGGER.error("",e);
            }
        });
    }

    public static List<Method> getAllMethod(Class<? extends Annotation> annotationClass){
        return getAll(annotationClass,annotationMethodsMap,(annotationData, list) -> {
            try {
                Arrays.stream(Class.forName(annotationData.clazz().getClassName()).getMethods()).filter(method -> {
                    return method.isAnnotationPresent(annotationClass);
                }).forEach(list::add);
            } catch (ClassNotFoundException e) {
                CreativeDrawersProducer2.LOGGER.error("",e);
            }
        });
    }

    public static <T> List<T> getAll(Class<? extends Annotation> annotationClass, Map<Class<? extends Annotation>, List<T>> map, BiConsumer<ModFileScanData.AnnotationData, List<T>> consumer){
        if(map.containsKey(annotationClass)){
            return map.get(annotationClass);
        }

        List<T> values = new ArrayList<>();
        Type type = Type.getType(annotationClass);
        ModList.get().getAllScanData().forEach(modData ->{
            modData.getAnnotations().forEach(annotationData -> {
                if(Objects.equals(type,annotationData.annotationType())){
                    consumer.accept(annotationData,values);
                }
            });
        });

        map.put(annotationClass,values);
        return values;
    }
}
