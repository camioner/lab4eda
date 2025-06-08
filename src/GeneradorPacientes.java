import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GeneradorPacientes {

    private static final String[] NOMBRES = {
            "Luis", "Matias", "Pedro", "Ana", "Felipe", "Juan", "Diego", "Sofía", "Carlos", "Lucia"
    };

    private static final String[] APELLIDOS = {
            "Gonzalez", "Aranguiz", "Lopez", "Martinez", "Bolados", "Sanchez", "Vidal", "Bravo", "Flores", "Paredes"
    };

    private static final String[] AREAS = {
            "SAPU", "urgencia_adulto", "infantil"
    };

    private static final Random RANDOM = new Random();

    public List<Paciente> generarPacientes(int n) {
        List<Paciente> pacientes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String nombre = NOMBRES[RANDOM.nextInt(NOMBRES.length)];
            String apellido = APELLIDOS[RANDOM.nextInt(APELLIDOS.length)];
            String id = String.format("ID%04d", i + 1);
            int categoria = generarCategoriaAleatoria();
            String area = AREAS[RANDOM.nextInt(AREAS.length)];

            Paciente p = new Paciente(nombre, apellido, id, categoria, area);
            pacientes.add(p);

            try {
                Thread.sleep(10); // Simula tiempo entre llegadas
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return pacientes;
    }

    public void guardarEnCSV(List<Paciente> pacientes, String nombreArchivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("nombre,apellido,id,categoria,tiempoLlegada,estado,area");
            for (Paciente p : pacientes) {
                pw.printf("%s,%s,%s,%d,%d,%s,%s%n",
                        p.getNombre(),
                        p.getApellido(),
                        p.getId(),
                        p.getCategoria(),
                        p.getTiempoLlegada(),
                        p.getEstado(),
                        p.area
                );
            }
        }
    }

    private int generarCategoriaAleatoria() {
        double valor = RANDOM.nextDouble();
        if (valor < 0.10) return 1;
        if (valor < 0.25) return 2;
        if (valor < 0.43) return 3;
        if (valor < 0.70) return 4;
        return 5;
    }

    public static void main(String[] args) {
        int n = 240;

        if (args.length >= 1) {
            n = Integer.parseInt(args[0]);
        }

        GeneradorPacientes gen = new GeneradorPacientes();
        List<Paciente> pacientes = gen.generarPacientes(n);
        String archivoSalida = "Pacientes_24h.csv";

        try {
            gen.guardarEnCSV(pacientes, archivoSalida);
            System.out.println("Archivo CSV generado: " + archivoSalida);
        } catch (IOException e) {
            System.err.println("Error al guardar archivo CSV: " + e.getMessage());
        }
    }
}
