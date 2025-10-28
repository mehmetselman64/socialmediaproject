package com.mehmetselman.socialmediaproject.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ✅ GlobalExceptionHandler
 *
 * Bu sınıf, tüm controller'lardaki istisnaları (exception) tek bir merkezde yakalar.
 *
 * - @RestControllerAdvice → Uygulamanın her yerindeki hataları global olarak dinler.
 * - @ExceptionHandler → Belirli türdeki exception'ları yakalar ve özel yanıt üretir.
 *
 * Avantajı:
 * ❯ Her hatayı tek tek try-catch içinde yazmana gerek kalmaz.
 * ❯ Frontend'e okunabilir, JSON formatında hata döner.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 🔹 RuntimeException yakalar (örneğin manuel throw edilen hatalar)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * 🔹 IllegalArgumentException (örneğin yanlış parametre, null değer vb.)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Geçersiz parametre: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * 🔹 NullPointerException (örneğin null dönen objelerde)
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointer(NullPointerException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Beklenmeyen bir null değeri oluştu!");
        error.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * 🔹 Diğer tüm exception türleri için genel yakalama
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Bir hata oluştu!");
        error.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
