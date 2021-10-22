package locadoradecarro;
public class LocadoraDeCarro {
    public static void main(String[] args) {
        Cliente c1 = new Cliente("33292033829", "Oliver", 21, "M", false);
        Carro f1 = new Carro("Ferrari", "FXG", "Vermelha", 2019, 200f, true, true);
        
        Alugueis alu = new Alugueis(c1, 5, 8, 9, 100.0f);
        System.out.println(alu.getValor());
        f1.mostrarCarros();
        c1.mostrarCliente();
    }
}
