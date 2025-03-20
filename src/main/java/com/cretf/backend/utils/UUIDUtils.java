package com.cretf.backend.utils;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.UUID;

public class UUIDUtils {
    public static UUID newTimeUUID() {
        // Lấy timestamp hiện tại theo số giây từ epoch (1970-01-01T00:00:00Z)
        long timestamp = Instant.now().toEpochMilli();

        // Chuyển timestamp thành bytes
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(timestamp);
        buffer.putLong(UUID.randomUUID().getLeastSignificantBits());

        // Tạo UUID từ byte array
        return UUID.nameUUIDFromBytes(buffer.array());
    }
}
