package com.tampn.rabbit_producer.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;


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
}
