package com.javyhuerta.consultorio.api.controller;

import com.javyhuerta.consultorio.api.model.DoctorModel;
import com.javyhuerta.consultorio.api.model.base.ApiResponseModel;
import com.javyhuerta.consultorio.api.model.base.Metadata;
import com.javyhuerta.consultorio.domain.specification.DoctoresSpecification;
import com.javyhuerta.consultorio.service.DoctoresService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
@RequestMapping("/doctores")
@Tag(name = "API Doctores", description = "Administración de doctores")
public class DoctoresController {

    private final DoctoresService service;

    @GetMapping
    @Operation(summary = "Obtiene la lista de doctores")
    @ApiResponse(responseCode = "200", description = "Lista de doctores",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DoctorModel.class)))
    public ResponseEntity<ApiResponseModel<List<DoctorModel>>> consultarDoctores(
            @Parameter(description = "Texto a buscar en nombre, apellidos del doctor o especialidad")
            @Valid @RequestParam(value = "q", required = false) String q,
            @Parameter(description = "Número de página (empezando en 0)")
            @Min(0)  @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Cantidad de elementos por página (mínimo 1)")
            @Min(1)  @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        DoctoresSpecification doctoresSpecification = DoctoresSpecification.builder().q(q).build();
        Page<DoctorModel> doctores = service.consultarDoctores(doctoresSpecification,pageable);

        ApiResponseModel<List<DoctorModel>> response = new ApiResponseModel<>(CodigoNegocio.EXITO,doctores.getContent());
        response.setMetadata(new Metadata(doctores));

        return ResponseEntity.ok(response);

    }


}
