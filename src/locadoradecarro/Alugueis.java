package locadoradecarro;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Alugueis{
    private final Set<Integer> id = new HashSet<>();
    private Cliente cliente;
    private int dataEntrega, dataDevolucao, dataRecebeu;
    private float valor;
    
    // Metodos Especiais
    public void listaAlugados(){
        
    }
    
     // Metodos Construtores, Getter and Setter
    public Alugueis(Cliente cliente, int dataEntrega, int dataDevolucao, int dataRecebeu, float valor) {
        if(contem(cliente)){
            setId();
            setCliente(cliente);
            this.dataEntrega = dataEntrega;
            this.dataDevolucao = dataDevolucao;
            this.dataRecebeu = dataRecebeu;
            this.valor = valor(valor, dataEntrega, dataDevolucao);
            System.out.println("Criou");
        }else{
            System.out.println("Não foi possivel efetuar o aluguel");
        }
    }
    
    private float valor(float valor, int dataE, int dataD){
        return valor * (float) (dataD - dataE);
    }
    
    private boolean contem(Cliente cliente){
        if(cliente == null || cliente.isTemCarro() == true){
            System.out.println("Cliente não cadastrado, ou cliente já possui carro em seu nome");
            return false;
        }else{
            return true;
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<Integer> getId() {
        return id;
    }

    private void setId() {
        Random random = new Random();
        this.id.add(random.nextInt());
    }
    
    public int getDataEntrega() {
        return dataEntrega;
    }
    public void setDataEntrega(int dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
    public int getDataDevolucao() {
        return dataDevolucao;
    }
    public void setDataDevolucao(int dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
    public int getDataRecebeu() {
        return dataRecebeu;
    }
    public void setDataRecebeu(int dataRecebeu) {
        this.dataRecebeu = dataRecebeu;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
}
