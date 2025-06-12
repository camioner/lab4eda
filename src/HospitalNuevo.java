import java.util.*;

public class HospitalNuevo {
    Map<String, Paciente> pacientesTotales;
    PriorityQueue<Paciente> colaAtencion;
    Map<String, AreaAtencion> areasAtencion;
    List<Paciente> pacientesAtendidos;
    Map<Integer, Integer> tiempoMaximoCategoria = Map.of(
            1, 5,
            2, 10,
            3, 15,
            4, 20,
            5, 25
    );

    public HospitalNuevo(){
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
    public Paciente obtenerPaciente(String id) {
        return pacientesTotales.get(id);
    }


    public Paciente atenderSiguiente(int minutoActual) {
        // Atención forzada si hay pacientes que superan su tiempo máximo
        for (Paciente p : colaAtencion) {
            int espera = minutoActual - (int) (p.getTiempoLlegada() / 60);
            int maxPermitido = tiempoMaximoCategoria.get(p.getCategoria());

            if (espera >= maxPermitido) {
                colaAtencion.remove(p); // Lo sacamos de la cola
                AreaAtencion area = areasAtencion.get(p.getArea());

                if (area != null && !area.estaSaturada()) {
                    area.ingresarPaciente(p);
                    p.cambiarEstado("atendido");
                    p.registrarCambio("Estado cambiado a atendido (forzado por tiempo máximo)");
                    pacientesAtendidos.add(p);
                    return p;
                } else {
                    colaAtencion.add(p); // reinsertamos si área está saturada
                    break;
                }
            }
        }

        // Si nadie excedió el tiempo, seguimos con el más urgente
        return atenderNormal();
    }

    private Paciente atenderNormal() {
        Paciente p = colaAtencion.poll();
        if (p == null) return null;

        AreaAtencion area = areasAtencion.get(p.area);
        if (area != null && !area.estaSaturada()) {
            area.ingresarPaciente(p);
            p.cambiarEstado("atendido");
            p.registrarCambio("Estado cambiado a atendido");
            pacientesAtendidos.add(p);
            return p;
        } else {
            colaAtencion.add(p); // reinsertamos si no se pudo atender
            return null;
        }
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
