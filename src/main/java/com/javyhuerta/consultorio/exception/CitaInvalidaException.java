package com.javyhuerta.consultorio.exception;

import com.javyhuerta.consultorio.util.CodigoNegocio;

public class CitaInvalidaException extends RuntimeException{


    public CitaInvalidaException(CodigoNegocio codigo){
        super(codigo.getCodigo());
    }

}
