import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class AreaAtencion {
    String Nombre;
    PriorityQueue<Paciente> pacientesHeap;
    int capacidadMaxima;


    AreaAtencion(String nombre, int capacidadMaxima) {

    }

    Paciente atenderPaciente(){
        return pacientesHeap.poll();
    }

    void ingresarPaciente(Paciente p){
        pacientesHeap.add(p);
    }

    boolean estaSaturada(){
        if (pacientesHeap.size() == capacidadMaxima){return true;}
        return false;
    }
    List<Paciente> obtenerPacientesPorHeapSort(){
        List<Paciente> pacientes = new ArrayList<>();

    }



    @Override
    public int compareTo(Paciente paciente,Paciente paciente2) {
        if(paciente.getCategoria() > paciente2.getCategoria()){
            return 1;
        }
        return 0;
    }

}