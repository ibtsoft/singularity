package com.ibtsoft.singularity.core;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class Singularity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Singularity.class);

    private final List<Class<?>> models = new ArrayList<>();

    public Singularity() {

    }

    public void addModelPackage(String classPath) {
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            LOGGER.debug("Class loader {}", cl );
            ClassPath path = ClassPath.from (cl);
            ImmutableSet<ClassInfo> classes = path.getTopLevelClassesRecursive(classPath);
            classes.forEach(classInfo -> {
                LOGGER.debug("Found class {}", classInfo.getName());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
