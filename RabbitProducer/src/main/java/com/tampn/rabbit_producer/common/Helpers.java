package com.tampn.rabbit_producer.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Helpers {

    public static byte[] toByteArray(Object obj) {
        if (Objects.nonNull(obj)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                return bos.toByteArray();
            } catch (IOException e) {
                return new byte[0];
            }
        }
        return new byte[0];
    }

    public static Object toObject(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
