package me.munchii.industrialreborn.core.store;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class StoreToken<T> {
    protected final String TOKEN;

    public StoreToken(String tok) {
        //noinspection unchecked
        //final Class<T> tokenClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        /*
        Type genericSuperClass = getClass().getGenericSuperclass();

        ParameterizedType parametrizedType = null;
        while (parametrizedType == null) {
            if ((genericSuperClass instanceof ParameterizedType)) {
                parametrizedType = (ParameterizedType) genericSuperClass;
            } else {
                genericSuperClass = ((Class<?>) genericSuperClass).getGenericSuperclass();
            }
        }

        //noinspection unchecked
        final Class<T> tokenClass = (Class<T>) parametrizedType.getActualTypeArguments()[0];

        TOKEN = tokenClass.getName();

         */

        TOKEN = tok;
    }
}
