package com.javyhuerta.consultorio.exception;

import com.javyhuerta.consultorio.api.model.base.ApiErrorModel;
import com.javyhuerta.consultorio.util.CodigoNegocio;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorModel> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.info("consultorio.GlobalExceptionHandler.handleValidationErrors");
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ApiErrorModel errorModel = new ApiErrorModel(CodigoNegocio.ERROR,errores);


        return ResponseEntity.badRequest().body(errorModel);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorModel> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.info("consultorio.GlobalExceptionHandler.handleTypeMismatch");
        String message = String.format("El parámetro '%s' debe ser del tipo '%s'",
                ex.getName(), ex.getRequiredType().getSimpleName());

        ApiErrorModel errorModel = new ApiErrorModel(CodigoNegocio.ERROR, List.of(message));

        return ResponseEntity.badRequest().body(errorModel);
    }

    @ExceptionHandler(CitaInvalidaException.class)
    public ResponseEntity<ApiErrorModel> citaInvalidaException(CitaInvalidaException ex) {

        CodigoNegocio codigoNegocio = CodigoNegocio.findByCode(ex.getMessage()).orElse(CodigoNegocio.ERROR);

        ApiErrorModel errorModel = new ApiErrorModel(codigoNegocio, List.of(codigoNegocio.getMensaje()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorModel);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorModel> handleAllExceptions(Exception ex) {
        log.info("consultorio.GlobalExceptionHandler.handleAllExceptions");
        ApiErrorModel errorModel = new ApiErrorModel(CodigoNegocio.ERROR, List.of("Error interno del servidor"));
        log.error(ex,ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
    }
}
