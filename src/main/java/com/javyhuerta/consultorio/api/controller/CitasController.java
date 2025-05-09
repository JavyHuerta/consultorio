package com.javyhuerta.consultorio.api.controller;

import com.javyhuerta.consultorio.api.model.CitaModel;
import com.javyhuerta.consultorio.api.model.DoctorModel;
import com.javyhuerta.consultorio.api.model.RegistrarCitaModel;
import com.javyhuerta.consultorio.api.model.base.ApiErrorModel;
import com.javyhuerta.consultorio.api.model.base.ApiResponseModel;
import com.javyhuerta.consultorio.api.model.base.Metadata;
import com.javyhuerta.consultorio.domain.specification.DoctoresSpecification;
import com.javyhuerta.consultorio.service.CitasService;
import com.javyhuerta.consultorio.util.CodigoNegocio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/citas")
@Tag(name = "API Citas", description = "Administración de citas")
public class CitasController {

    private final CitasService service;

    @PostMapping
    @Operation(summary = "Registro de citas")
    @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorModel.class)))
    @ApiResponse(responseCode = "201", description = "Registra un autor",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CitaModel.class)))
    public ResponseEntity<ApiResponseModel<CitaModel>> registrarCita(
            @Valid @RequestBody RegistrarCitaModel cita){
        ApiResponseModel<CitaModel> response = new ApiResponseModel<>(CodigoNegocio.EXITO,service.registrarCita(cita));
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @GetMapping
    @Operation(summary = "Obtiene la lista de citas")
    @ApiResponse(responseCode = "200", description = "Lista de doctores",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CitaModel.class)))
    public ResponseEntity<ApiResponseModel<List<CitaModel>>> consultarCitas(
            @Parameter(description = "Texto a buscar en nombre, apellidos del doctor o especialidad")
            @Valid @RequestParam(value = "q", required = false) String q,
            @Parameter(description = "Número de página (empezando en 0)")
            @Min(0)  @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Cantidad de elementos por página (mínimo 1)")
            @Min(1)  @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        //DoctoresSpecification doctoresSpecification = DoctoresSpecification.builder().q(q).build();
        Page<CitaModel> citas = service.consultarCitas(pageable);

        ApiResponseModel<List<CitaModel>> response = new ApiResponseModel<>(CodigoNegocio.EXITO,citas.getContent());
        response.setMetadata(new Metadata(citas));

        return ResponseEntity.ok(response);

    }
}
