package locadoradecarro;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Carro {
    private Set<Integer> id = new HashSet<>();
    private String marca;
    private String modelo;
    private String cor;
    private int ano;
    private float valor;
    private boolean arCondicionado;
    private boolean disponivel;

    // Metodos Especiais
    public void editar(){
        
    }
    public void excluir(){
        
    }
    public void mostrarCarros(){
        System.out.println("---------------Carro---------------");
        System.out.print("Marca:" + getMarca() + " Medelo: " + getModelo() + " Ano: " + getAno() + "\nCor: " + getCor() +
                " Valor/dia: " + getValor() + " Possui Ar: " + isArCondicionado() + "\nDisponivel: " + isDisponivel() + "\n");
    }

    
    // Metodos Construtores, Getter and Setter
    public Carro(String marca, String modelo, String cor, int ano, 
                            float valor, boolean arCondicionado, boolean disponivel) {
        setId();
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.ano = ano;
        this.valor = valor;
        this.arCondicionado = arCondicionado;
        this.disponivel = disponivel;
    }
    public Set<Integer> getId() {
        return id;
    }
    private void setId() {
        Random random = new Random();
        this.id.add(random.nextInt());
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getCor() {
        return cor;
    }
    public void setCor(String cor) {
        this.cor = cor;
    }
    public int getAno() {
        return ano;
    }
    public void setAno(int ano) {
        this.ano = ano;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public boolean isArCondicionado() {
        return arCondicionado;
    }
    public void setArCondicionado(boolean arCondicionado) {
        this.arCondicionado = arCondicionado;
    }
    public boolean isDisponivel() {
        return disponivel;
    }
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
