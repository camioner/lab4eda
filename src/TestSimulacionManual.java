import java.util.*;
import java.io.*;

public class TestSimulacionManual {
    public static void main(String[] args) throws IOException {
        Hospital hospital = new Hospital();
        LectorPacientes lector = new LectorPacientes();
        List<Paciente> pacientes = lector.leerDesdeCSV("Pacientes_24h.csv");

        // Estadísticas
        Map<String, Long> tiemposAtencion = new HashMap<>();
        Map<Integer, Integer> atendidosPorCategoria = new HashMap<>();
        Map<Integer, List<Long>> tiemposPorCategoria = new HashMap<>();
        List<Paciente> pacientesFueraDeTiempo = new ArrayList<>();

        Map<Integer, Integer> tiempoMaximoCategoria = Map.of(
                1, 0,
                2, 30,
                3, 90,
                4, 180,
                5, Integer.MAX_VALUE
        );

        int minuto = 0;
        int pacienteIndex = 0;
        int nuevosPacientesAcumulados = 0;

        for (minuto = 0; minuto < 1440; minuto++) {

            // Llega nuevo paciente cada 10 minutos
            if (minuto % 10 == 0 && pacienteIndex < pacientes.size()) {
                Paciente nuevo = pacientes.get(pacienteIndex++);
                hospital.registrarPaciente(nuevo);
                nuevosPacientesAcumulados++;
            }

            // Atender paciente cada 15 minutos
            if (minuto % 15 == 0) {
                atenderYRegistrar(hospital, tiemposAtencion, atendidosPorCategoria, tiemposPorCategoria, pacientesFueraDeTiempo, tiempoMaximoCategoria);
            }

            // Si se acumulan 3 nuevos pacientes: atender 2 extra
            if (nuevosPacientesAcumulados >= 3) {
                atenderYRegistrar(hospital, tiemposAtencion, atendidosPorCategoria, tiemposPorCategoria, pacientesFueraDeTiempo, tiempoMaximoCategoria);
                atenderYRegistrar(hospital, tiemposAtencion, atendidosPorCategoria, tiemposPorCategoria, pacientesFueraDeTiempo, tiempoMaximoCategoria);
                nuevosPacientesAcumulados = 0;
            }
        }

        // Mostrar resultados
        System.out.println("\n=== Resultados de la simulación ===");
        System.out.println("Pacientes atendidos: " + tiemposAtencion.size());

        for (int cat = 1; cat <= 5; cat++) {
            int cantidad = atendidosPorCategoria.getOrDefault(cat, 0);
            List<Long> tiempos = tiemposPorCategoria.getOrDefault(cat, List.of());
            double promedio = tiempos.stream().mapToLong(Long::longValue).average().orElse(0);
            System.out.printf("Categoría C%d: %d pacientes, espera promedio %.2f min\n", cat, cantidad, promedio);
        }

        System.out.println("\nPacientes que excedieron el tiempo máximo:");
        for (Paciente p : pacientesFueraDeTiempo) {
            System.out.printf("- %s %s (C%d, espera %d min)\n",
                    p.getNombre(), p.getApellido(), p.getCategoria(), p.tiempoEsperaActual());
        }
    }

    private static void atenderYRegistrar(
            Hospital hospital,
            Map<String, Long> tiemposAtencion,
            Map<Integer, Integer> atendidosPorCategoria,
            Map<Integer, List<Long>> tiemposPorCategoria,
            List<Paciente> pacientesFueraDeTiempo,
            Map<Integer, Integer> tiempoMaximoCategoria
    ) {
        Paciente p = hospital.atenderSiguiente();
        if (p != null) {
            long tiempoEspera = p.tiempoEsperaActual();
            tiemposAtencion.put(p.getId(), tiempoEspera);

            atendidosPorCategoria.put(p.getCategoria(),
                    atendidosPorCategoria.getOrDefault(p.getCategoria(), 0) + 1);

            tiemposPorCategoria.putIfAbsent(p.getCategoria(), new ArrayList<>());
            tiemposPorCategoria.get(p.getCategoria()).add(tiempoEspera);

            if (tiempoEspera > tiempoMaximoCategoria.get(p.getCategoria())) {
                pacientesFueraDeTiempo.add(p);
            }
        }
    }
}