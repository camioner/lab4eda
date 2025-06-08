public class Test {
    public static void main(String[] args) throws InterruptedException {
        AreaAtencion sapu = new AreaAtencion("SAPU", 10);

        Paciente p1 = new Paciente("Ana", "Lopez", "1", 2, "SAPU");
        Thread.sleep(1000); // Simula tiempo
        Paciente p2 = new Paciente("Benito", "Rojas", "2", 1, "SAPU");
        Thread.sleep(1000);
        Paciente p3 = new Paciente("Carmen", "Díaz", "3", 3, "SAPU");

        sapu.ingresarPaciente(p1);
        sapu.ingresarPaciente(p2);
        sapu.ingresarPaciente(p3);

        System.out.println("Orden por HeapSort:");
        for (Paciente p : sapu.obtenerPacientesPorHeapSort()) {
            System.out.println(p);
        }

        System.out.println("Atendiendo paciente más urgente:");
        System.out.println(sapu.atenderPaciente());
    }
}