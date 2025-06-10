
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AreaAtencion {
    String nombre;
    PriorityQueue<Paciente> pacientesHeap;
    int capacidadMaxima;


    public AreaAtencion(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.pacientesHeap = new PriorityQueue<>(PRIORIDAD_URGENCIA);
    }

    Paciente atenderPaciente() {
        return pacientesHeap.poll();
    }

    void ingresarPaciente(Paciente p) {
        pacientesHeap.add(p);
    }

    boolean estaSaturada() {
        if (pacientesHeap.size() == capacidadMaxima) {
            return true;
        }
        return false;
    }

    List<Paciente> obtenerPacientesPorHeapSort() {
        List<Paciente> pacientes = new ArrayList<>(pacientesHeap);
        pacientes.sort(PRIORIDAD_URGENCIA);
        return pacientes;
    }

    public static final Comparator<Paciente> PRIORIDAD_URGENCIA = new Comparator<Paciente>() {
        @Override
        public int compare(Paciente p1, Paciente p2) {
            if (p1.categoria != p2.categoria) {
                return Integer.compare(p1.categoria, p2.categoria); // menor = más urgente
            } else {
                return Long.compare(p1.tiempoLlegada, p2.tiempoLlegada); // más tiempo esperando
            }
        }
    };


}

