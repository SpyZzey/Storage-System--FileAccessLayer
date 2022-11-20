package de.storagesystem.api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Objects;

/**
 * @author Simon Brebeck
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpClientErrorException(HttpClientErrorException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getMessage())));
    }

    /**
     * Handles {@link InvalidTokenException} and returns a 401 status code with an error message.
     * @param e {@link InvalidTokenException} to handle
     * @return A {@link ResponseEntity} with a 401 status code
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(401).body(Map.of(
                "status", "error",
                "message", e.getMessage()));
    }

    /**
     * Handles {@link UserInputValidationException} and returns a 400 status code with an error message.
     * @param e {@link UserInputValidationException} to handle
     * @return A {@link ResponseEntity} with a 400 status code
     */
    @ExceptionHandler(UserInputValidationException.class)
    public ResponseEntity<Map<String, String>> handleUserInputValidationException(UserInputValidationException e) {
        logger.info(e.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()));
    }

    /**
     * Handles {@link StorageEntityNotFoundException} and returns a 400 status code with an error message.
     * @param e {@link StorageEntityNotFoundException} to handle
     * @return A {@link ResponseEntity} with a 400 status code
     */
    @ExceptionHandler(StorageEntityNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageEntityNotFoundException e) {
        logger.info("Storage Entity was not found");
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()));
    }

    /**
     * Handles {@link StorageEntityAlreadyExistsException} and returns a 400 status code with an error message.
     * @param e {@link StorageEntityAlreadyExistsException} to handle
     * @return A {@link ResponseEntity} with a 400 status code
     */
    @ExceptionHandler(StorageEntityAlreadyExistsException.class)
    public ResponseEntity<?> handleStorageFileAlreadyExists(StorageEntityAlreadyExistsException e) {
        logger.info(e.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()));
    }

    /**
     * Handles {@link MaxUploadSizeExceededException} and returns a 413 status code with an error message.
     * @param e {@link MaxUploadSizeExceededException} to handle
     * @return A {@link ResponseEntity} with a 413 status code
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleFileSizeLimitExceeded(MaxUploadSizeExceededException e) {
        logger.info("File size limit exceeded. Max: " + e.getMaxUploadSize());
        return ResponseEntity.status(413).body("File size limit exceeded. Max: " + e.getMaxUploadSize());
    }
}
