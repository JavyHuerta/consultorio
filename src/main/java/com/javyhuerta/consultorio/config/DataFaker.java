package com.javyhuerta.consultorio.config;

import com.javyhuerta.consultorio.domain.entity.Doctor;
import com.javyhuerta.consultorio.domain.repository.DoctoresRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Data
@Log4j2
@Component
public class DataFaker  implements CommandLineRunner {

    private final DoctoresRepository doctoresRepository;
    private final Random RANDOM = new Random();
    private final Faker faker;

    @Override
    public void run(String... args) throws Exception {
        generarDoctoresFake();

        log.info("Se han insertado datos de prueba");
    }

    private void generarDoctoresFake(){
        for (int i = 0; i < 50; i++) {
            Doctor doctor = new Doctor();
            doctor.setNombre(faker.name().firstName());
            doctor.setApellidoPaterno(faker.name().lastName());
            doctor.setApellidoMaterno(faker.name().lastName());
            doctor.setEspecialidad(getEspecialidadAleatoria());

            doctoresRepository.save(doctor);
        }
    }

    private List<String> ESPECIALIDADES = List.of(
            "Cardiología", "Dermatología", "Pediatría", "Neurología",
            "Ginecología", "Oncología", "Psiquiatría", "Urología",
            "Oftalmología", "Traumatología", "Medicina Interna"
    );

    public String getEspecialidadAleatoria() {
        return ESPECIALIDADES.get(RANDOM.nextInt(ESPECIALIDADES.size()));
    }
}
