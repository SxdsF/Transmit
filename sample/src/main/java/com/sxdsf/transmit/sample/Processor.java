package com.sxdsf.transmit.sample;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by sunbowen on 2015/12/19.
 */
public class Processor {
    public static void autoBindView(Activity activity) {
        if (activity != null) {
            Class<?> cls = activity.getClass();
            if (cls != null) {
                Field[] fields = cls.getDeclaredFields();
                if (fields != null) {
                    for (Field field : fields) {
                        if (field != null) {
                            if (field.isAnnotationPresent(BindView.class)) {
                                BindView bv = field.getAnnotation(BindView.class);
                                if (bv != null) {
                                    field.setAccessible(true);
                                    try {
                                        field.set(activity, activity.findViewById(bv.value()));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
