package cl.duoc.AppFiesta.dataloader;

import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cl.duoc.AppFiesta.model.Comuna;
import cl.duoc.AppFiesta.model.Fiesta;
import cl.duoc.AppFiesta.repository.ComunaRepository;
import cl.duoc.AppFiesta.repository.FiestaRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Profile("dev") // Solo corre si el perfil activo es DEV
@Component // Esta clase forma parte de la aplicación, créala automáticamente como un Bean.
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner { // CommandLineRunner Cuando termina de levantar la aplicación

    private final FiestaRepository fiestaRepository;
    private final ComunaRepository comunaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        // Random random = new Random();

        // Generar Fiestas
        for (int i = 0; i < 50; i++) {
            Fiesta fiesta = new Fiesta();
            Comuna comuna = comunaRepository.findById(ThreadLocalRandom.current().nextLong(1, 4))
                    .orElseThrow();
            ;
            fiesta.setNombre(faker.name().fullName());
            fiesta.setTipoDeFiesta(faker.options().option(
                    "Cumpleaños",
                    "Matrimonio",
                    "Graduación",
                    "Fiesta corporativa",
                    "Aniversario"));
            fiesta.setFechaRealizacion(
                    faker.timeAndDate()
                            .future(365, TimeUnit.DAYS)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
            fiesta.setDireccion(faker.address().fullAddress());
            fiesta.setComuna(comuna);
            fiesta.setCapacidad(ThreadLocalRandom.current().nextInt(50, 200));

            fiestaRepository.save(fiesta);
        }
    }
}
