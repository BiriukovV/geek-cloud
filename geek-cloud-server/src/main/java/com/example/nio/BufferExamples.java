package com.example.nio;

import java.nio.ByteBuffer;

public class BufferExamples {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 65);
        buffer.put((byte) 66);

        buffer.flip();

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }

//        buffer.put((byte) 67);

        buffer.rewind();

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }

    }
}
