package net.yxiao233.cdp2.util;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <T, C> T getPrivateField(String memberName, Class<T> typeClass, @Nullable Object targetObject, Class<C> targetClass){
        try {
            if(targetObject != null && !targetClass.isAssignableFrom(targetObject.getClass())){
                return null;
            }
            Field field = targetClass.getDeclaredField(memberName);
            field.setAccessible(true);
            Object object = field.get(targetObject);
            if(object == null){
                return null;
            }
            if(typeClass.isAssignableFrom(object.getClass())){
                return (T) object;
            }else{
                return null;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, C> void setPrivateField(String memberName, Class<T> typeClass, @Nullable Object targetObject, Class<C> targetClass, T newValue){
        try {
            if(targetObject != null && !targetClass.isAssignableFrom(targetObject.getClass())){
                return;
            }
            Field field = targetClass.getDeclaredField(memberName);
            field.setAccessible(true);
            Object object = field.get(targetObject);
            if(object == null || typeClass.isAssignableFrom(object.getClass())){
                field.set(targetObject,newValue);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public static Enum<?> getPackagedEnumField(String memberName, @Nullable Object targetObject, Class<?> targetClass){
        try {
            if(targetObject != null && !targetClass.isAssignableFrom(targetObject.getClass())){
                return null;
            }
            Field field = targetClass.getDeclaredField(memberName);
            field.setAccessible(true);
            Object object = field.get(targetObject);
            if(object != null){
                return Enum.valueOf((Class<? extends Enum>) object.getClass(), ((Enum) object).name());
            }
            return null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public static void setPackagedEnumField(String memberName, @Nullable Object targetObject, Class<?> targetClass, String newValue){
        try {
            if(targetObject != null && !targetClass.isAssignableFrom(targetObject.getClass())){
                return;
            }
            Field field = targetClass.getDeclaredField(memberName);
            field.setAccessible(true);
            Object object = field.get(targetObject);
            if(object != null){
                Enum value = Enum.valueOf((Class<? extends Enum>) object.getClass(), newValue);
                field.set(targetObject,value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <C> Method getPrivateMethod(String methodName, Class<?>[] parameterTypeClasses, Class<C> targetClass){
        try {
            Method method = targetClass.getDeclaredMethod(methodName, parameterTypeClasses);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <C, R> R runPrivateMethod(String methodName, Class<?>[] parameterTypeClasses, C targetObject, Class<C> targetClass, Object[] methodParameterValues, @Nullable Class<R> returnTypeClass){
        Method method = getPrivateMethod(methodName, parameterTypeClasses, targetClass);
        try {
            Object invoke = method.invoke(targetObject, methodParameterValues);
            if(returnTypeClass == null){
                return null;
            }
            if(returnTypeClass.isAssignableFrom(invoke.getClass())){
                return (R) invoke;
            }else{
                return null;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

