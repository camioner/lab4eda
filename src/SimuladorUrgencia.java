import java.io.*;
import java.util.*;

public class SimuladorUrgencia {

    public void simular(int pacientesPorDia) {
        Hospital hospital = new Hospital();
        LectorPacientes lector = new LectorPacientes();
        String idSeguimiento = "ID0075"; // ID de paciente C4
        Map<String, Integer> minutoLlegadaSeguimiento = new HashMap<>();


        List<Paciente> pacientes;
        try {
            pacientes = lector.leerDesdeCSV("Pacientes_24h.csv");
        } catch (IOException e) {
            System.err.println("Error leyendo CSV: " + e.getMessage());
            return;
        }

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



            // Llegada cada 10 min
            if (minuto % 10 == 0 && pacienteIndex < pacientesPorDia && pacienteIndex < pacientes.size()) {
                Paciente nuevo = pacientes.get(pacienteIndex++);
                hospital.registrarPaciente(nuevo);
                nuevosPacientesAcumulados++;
                if (nuevo.getId().equals(idSeguimiento)) {
                    minutoLlegadaSeguimiento.put(idSeguimiento, minuto);
                }
            }

            // Atención cada 15 min
            if (minuto % 15 == 0) {
                atenderYRegistrar(hospital, minuto, tiemposAtencion, atendidosPorCategoria,
                        tiemposPorCategoria, pacientesFueraDeTiempo, tiempoMaximoCategoria,
                        idSeguimiento, minutoLlegadaSeguimiento);
            }

            // Atención extra cada 3 nuevos
            if (nuevosPacientesAcumulados >= 3) {
                atenderYRegistrar(hospital, minuto, tiemposAtencion, atendidosPorCategoria,
                        tiemposPorCategoria, pacientesFueraDeTiempo, tiempoMaximoCategoria,
                        idSeguimiento, minutoLlegadaSeguimiento);
                atenderYRegistrar(hospital, minuto, tiemposAtencion, atendidosPorCategoria,
                        tiemposPorCategoria, pacientesFueraDeTiempo, tiempoMaximoCategoria,
                        idSeguimiento, minutoLlegadaSeguimiento);
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
            int llegadaMinuto = (int) (p.getTiempoLlegada() / 60);
            long espera = 1440 - llegadaMinuto;
            System.out.printf("- %s %s (C%d, espera %d min)\n",
                    p.getNombre(), p.getApellido(), p.getCategoria(), espera);
        }
    }

    private void atenderYRegistrar(
            Hospital hospital,
            int minutoActual,
            Map<String, Long> tiemposAtencion,
            Map<Integer, Integer> atendidosPorCategoria,
            Map<Integer, List<Long>> tiemposPorCategoria,
            List<Paciente> pacientesFueraDeTiempo,
            Map<Integer, Integer> tiempoMaximoCategoria,
            String idSeguimiento,
            Map<String, Integer> minutoLlegadaSeguimiento
    ) {
        Paciente p = hospital.atenderSiguiente();

        if (p != null) {
            int minutoLlegada = (int) (p.getTiempoLlegada() / 60);
            long tiempoEspera = minutoActual - minutoLlegada;

            tiemposAtencion.put(p.getId(), tiempoEspera);
            atendidosPorCategoria.put(p.getCategoria(),
                    atendidosPorCategoria.getOrDefault(p.getCategoria(), 0) + 1);

            tiemposPorCategoria.putIfAbsent(p.getCategoria(), new ArrayList<>());
            tiemposPorCategoria.get(p.getCategoria()).add(tiempoEspera);

            if (tiempoEspera > tiempoMaximoCategoria.get(p.getCategoria())) {
                pacientesFueraDeTiempo.add(p);
            }
            if (p.getId().equals(idSeguimiento)) {
                int llegada = minutoLlegadaSeguimiento.getOrDefault(idSeguimiento, -1);
                if (llegada != -1) {
                    System.out.printf("\n[SEGUIMIENTO] Paciente %s fue atendido en el minuto %d (esperó %d min)\n",
                            idSeguimiento, minutoActual, minutoActual - llegada);
                }
            }


        }
    }
}