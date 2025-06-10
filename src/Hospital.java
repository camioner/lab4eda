import java.util.*;

public class Hospital {
    Map<String, Paciente> pacientesTotales;
    PriorityQueue<Paciente> colaAtencion;
    Map<String, AreaAtencion> areasAtencion;
    List<Paciente> pacientesAtendidos;

    public Hospital(){
        pacientesTotales = new HashMap<String, Paciente>();
        areasAtencion = new HashMap<>();
        pacientesAtendidos = new ArrayList<>();

        // Inicializamos las 3 áreas del hospital
        areasAtencion.put("SAPU", new AreaAtencion("SAPU", 100));
        areasAtencion.put("urgencia_adulto", new AreaAtencion("urgencia_adulto", 100));
        areasAtencion.put("infantil", new AreaAtencion("infantil", 100));

        // Cola central de atención: se ordena por prioridad (categoría + tiempo de llegada)
        colaAtencion = new PriorityQueue<>(AreaAtencion.PRIORIDAD_URGENCIA);
    }

    public void registrarPaciente(Paciente paciente) {
        if (paciente.getArea() == null || paciente.getArea().isBlank()) {
            asignarAreaDisponible(paciente);
        }
        pacientesTotales.put(paciente.getId(), paciente);
        colaAtencion.add(paciente);
    }

    public void reasignarCategoria(String id, int nuevaCategoria) {
        Paciente p = pacientesTotales.get(id);
        if (p != null) {
            p.registrarCambio("Categoría cambiada de " + p.getCategoria() + " a " + nuevaCategoria);
            p.categoria = nuevaCategoria;
            // Para que tome efecto en la cola, puedes volver a insertarlo:
            colaAtencion.remove(p);
            colaAtencion.add(p);
        }
    }

    public Paciente atenderSiguiente() {
        Paciente p = colaAtencion.poll(); // paciente más urgente
        if (p == null) return null;

        AreaAtencion area = areasAtencion.get(p.area);
        if (area != null && !area.estaSaturada()) {
            area.ingresarPaciente(p);
            p.cambiarEstado("atendido");
            pacientesAtendidos.add(p);
            return p;
        } else {
            // Si área saturada, podrías reinsertarlo a la cola o manejarlo distinto
            colaAtencion.add(p);
        }
        return null;
    }

    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {
        List<Paciente> resultado = new ArrayList<>();
        for (Paciente p : colaAtencion) {
            if (p.getCategoria() == categoria && p.getEstado().equals("en_espera")) {
                resultado.add(p);
            }
        }
        return resultado;
    }


    public AreaAtencion obtenerArea(String nombre) {
        return areasAtencion.get(nombre);
    }
    private void asignarAreaDisponible(Paciente paciente) {
        for (AreaAtencion area : areasAtencion.values()) {
            if (!area.estaSaturada()) {
                paciente.setArea(area.nombre); // Asigna el nombre del área
                return;
            }
        }

        // Si todas están saturadas, asigna una al azar
        List<String> nombres = new ArrayList<>(areasAtencion.keySet());
        String aleatoria = nombres.get(new Random().nextInt(nombres.size()));
        paciente.setArea(aleatoria);
    }


}
