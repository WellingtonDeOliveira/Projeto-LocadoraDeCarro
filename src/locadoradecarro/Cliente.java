package locadoradecarro;
public class Cliente {
    private String cpf;
    private String nome;
    private int idade;
    private String sexo;
    private boolean temCarro;
    private Alugueis lista;
    
    
    // Metodos Especiais
    public void mostrarCliente(){
        System.out.println("Cliente: " + getNome() + " CPF: " + ocultarCpf(getCpf()) + " Idade:" + getIdade());
    }
    
    public void mostrarListaCar(int cpf){
        
    }
    public void excluirCliente(int cpf){
        
    }
    
    public String ocultarCpf(String cpf){
        String s[] = new String[14], oculto = "";
        for(int i = 0; i<11; i++){
            s[i] = cpf.substring(i, i+1);
        }
        for(int i = 3; i<14; i++){
            s[i] = "*";
        }
        s[7] = s[3] = ".";
        s[11] = "-";
        for(int i = 0; i<14; i++){
            oculto += s[i];
        }
        
        return oculto;
    }
    
    public String verificarCpf(String cpf){
        String s[] = new String[9], confere = "";
        int n[] = new int[9], verificador1 = 0, verificador2, con = 0;
        
        for(int i = 0; i<9; i++){
            s[i] = cpf.substring(i, i+1); n[i] = Integer.parseInt(s[i]);
        }
        for(int i = 10; i>=2; i--){
            verificador1 += (n[con] * i);
            con++;
        }
        con = 0;
        if((verificador1 % 11) < 2){
            verificador1 = 0;
        }else{
            verificador1 = 11 - (verificador1 % 11);
        }
        verificador2 = verificador1 * 2;
        for(int i = 11; i>=3; i--){
            verificador2 += (n[con] * i);
            con++;
        }
        if((verificador2 % 11) < 2){
            verificador2 = 0;
        }else{
            verificador2 = 11 - (verificador2 % 11);
        }
       confere = (s[0]+ s[1] + s[2] + s[3] + s[4] + s[5] + s[6] + s[7] + s[8] + verificador1 + "" + verificador2);
        if(confere.equals(cpf)){
            System.out.println("CPF CORRETO");
        }else{
            System.out.println("CPF INVALIDO e CORRIGIDO: " + confere);
        }
        return confere;
    }
    
    
    // Metodos Construtores, Getter and Setter
    public Cliente(String cpf, String nome, int idade, String sexo, boolean temCarro) {
        setCpf(cpf);
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.temCarro = temCarro;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = verificarCpf(cpf);
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getIdade() {
        return idade;
    }
    public void setIdade(int idade) {
        this.idade = idade;
    }
    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public boolean isTemCarro() {
        return temCarro;
    }
    public void setTemCarro(boolean temCarro) {
        this.temCarro = temCarro;
    }
}
