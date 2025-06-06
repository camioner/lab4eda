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

    Paciente(String nombre, String apellido, String id, int categoria, String area) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.tiempoLlegada = System.currentTimeMillis();
        this.estado = "en_espera";
        this.historialCambios = new Stack<>();
        this.area = area;


    }

    long tiempoEsperaActual() {
        return (System.currentTimeMillis() - tiempoLlegada) / 60000; // en minutos
    }


    void registrarCambio(String descripcion) {
        historialCambios.push(descripcion);
    }
    int getCategoria() {
        return categoria;
    }
    String getEstado(){
        return estado;
    }
    void cambiarEstado(String estado){
        this.estado=estado;
    }
    String obtenerUltimoCambio() {
        return historialCambios.pop();
    }


}