import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class GeneradorPacientes {

    private static final String[] NOMBRES = {
            "Luis", "Matias", "Pedro", "Ana", "Felipe", "Juan", "Diego", "Sofía", "Carlos", "Lucia"
    };
    private static final String[] APELLIDOS = {
            "Gonzalez", "Aranguiz", "Lopez", "Martinez", "Bolados", "Sanchez", "Vidal", "Bravo", "Flores", "Paredes"
    };

    private static final Random RANDOM = new Random();

    public List<Paciente> generarPacientes(int n, long timestampInicio) {
        List<Paciente> pacientes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String nombre = NOMBRES[RANDOM.nextInt(NOMBRES.length)];
            String apellido = APELLIDOS[RANDOM.nextInt(APELLIDOS.length)];
            String id = String.format("ID%04d", i + 1);
            int categoria = generarCategoriaAleatoria();
            long tiempoLlegada = timestampInicio + i * 600; // cada 10 minutos = 600 segundos
            Paciente p = new Paciente(nombre, apellido, id, categoria);
            p.cambiarEstado("en_espera");
            pacientes.add(p);
        }
        return pacientes;
    }


    public void guardarEnCSV(List<Paciente> pacientes, String nombreArchivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("nombre,apellido,id,categoria,tiempoLlegada,estado");
            for (Paciente p : pacientes) {
                pw.printf("%s,%s,%s,%d,%d,%s%n",
                        p.getNombre(),
                        p.getApellido(),
                        p.getId(),
                        p.getCategoria(),
                        p.getTiempoLlegada(),
                        p.getEstado()
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
        long timestampInicio = 0L;
        if (args.length >= 1) {
            n = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) {
            timestampInicio = Long.parseLong(args[1]);
        }
        GeneradorPacientes gen = new GeneradorPacientes();
        List<Paciente> pacientes = gen.generarPacientes(n, timestampInicio);
        String archivoSalida = "Pacientes_24h.csv";
        try {
            gen.guardarEnCSV(pacientes, archivoSalida);
            System.out.println("Archivo CSV generado: " + archivoSalida);
        } catch (IOException e) {
            System.err.println("Error al guardar archivo CSV: " + e.getMessage());
        }
    }
}