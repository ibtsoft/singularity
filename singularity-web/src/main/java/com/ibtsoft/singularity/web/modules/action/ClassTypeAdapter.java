package com.ibtsoft.singularity.web.modules.action;

import java.io.IOException;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class ClassTypeAdapter extends TypeAdapter<Class<?>> {

    // The type adapter does not hold state, so it can be easily made singleton (+ making the constructor private)
    private static final TypeAdapter<Class<?>> INSTANCE = new ClassTypeAdapter()
        // This is a convenient method that can do trivial null-checks in write(...)/read(...) itself
        .nullSafe();

    private ClassTypeAdapter() {
    }

    public static TypeAdapter<Class<?>> get() {
        return INSTANCE;
    }

    @Override
    public void write(final JsonWriter out, final Class<?> value) throws IOException {
        // value is never a null here
        out.value(value.getName());
    }

    @Override
    public Class<?> read(final JsonReader in) throws IOException {
        try {
            // This will never be a null since nullSafe() is used above
            final String className = in.nextString();
            return Class.forName(className);
        } catch (final ClassNotFoundException ex) {
            // No need to duplicate the message generated in ClassNotFoundException
            throw new JsonParseException(ex);
        }
    }
}
