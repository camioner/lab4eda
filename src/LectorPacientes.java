import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorPacientes {

    public List<Paciente> leerDesdeCSV(String nombreArchivo) throws IOException {
        List<Paciente> pacientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea = br.readLine(); // salta encabezado

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");

                String nombre = partes[0];
                String apellido = partes[1];
                String id = partes[2];
                int categoria = Integer.parseInt(partes[3]);
                long tiempoLlegada = Long.parseLong(partes[4]);
                String estado = partes[5];


                Paciente p = new Paciente(nombre, apellido, id, categoria, tiempoLlegada);

                if (!estado.equals("en_espera")) {
                    p.cambiarEstado(estado); // solo si estaba marcado como atendido, etc.
                }

                pacientes.add(p);
            }
        }

        return pacientes;
    }
}
