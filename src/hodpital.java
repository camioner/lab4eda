import java.util.*;

public class hodpital {
    Map<String,Paciente> pacientestotales = new HashMap<>();
    PriorityQueue<Paciente> colaatencion = new PriorityQueue<>();
    Map<String, AreaAtencion> areaatencion = new HashMap<>();
    ArrayList<Paciente> pacientesatendidos = new ArrayList<>();



    public void registrarPacientes(Paciente p){
        colaatencion.add(p);

    }
}
