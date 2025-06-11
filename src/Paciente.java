import java.util.Stack;

public class Paciente {
    String nombre;
    String apellido;
    String id;
    int categoria;
    long tiempoLlegada;
    String estado;
    String area;
    Stack<String> historialCambios;

    // Constructor completo con área
    public Paciente(String nombre, String apellido, String id, int categoria, long tiempoLlegada) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.area = null;
        this.tiempoLlegada = tiempoLlegada;
        this.estado = "en_espera";
        this.historialCambios = new Stack<>();
    }

    long tiempoEsperaActual() {
        return (System.currentTimeMillis() - tiempoLlegada) / 60000; // en minutos
    }

    void registrarCambio(String descripcion) {
        historialCambios.push(descripcion);
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getId() { return id; }
    public int getCategoria() { return categoria; }
    public long getTiempoLlegada() { return tiempoLlegada; }
    public String getEstado() { return estado; }
    public String getArea() { return area; }

    public void setArea(String area) {
        this.area = area;
    }

    void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        registrarCambio("Estado cambiado a " + nuevoEstado);
    }

    String obtenerUltimoCambio() {
        return historialCambios.isEmpty() ? "Sin historial" : historialCambios.pop();
    }

    @Override
    public String toString() {
        return String.format("%s %s (ID: %s, C%d, %s, estado: %s)", nombre, apellido, id, categoria, area, estado);
    }
}