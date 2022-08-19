package Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Solicitacao {

    //Variaveis
    private Date inicio, fim;
    private String embarcacao, local, itemSimples, itemUrgente;
    private String[] cameras, internet, telefonia;

    //Consrução do Objeto
    public Solicitacao(
            String inicio, String fim,
            String embarcacao, String local,
            String[] cameras, String[] internet, String[] telefonia,
            String itemSimples, String itemUrgente) {
        try { //tentando formatar a data para o jeito simples
            this.inicio = new SimpleDateFormat("dd/MM/yyyy").parse(inicio);
            this.fim = new SimpleDateFormat("dd/MM/yyyy").parse(fim);
        } catch (ParseException ex) {
            System.err.println("Erro ao gerar Data na classe Solicitacao.");
            Logger.getLogger(Solicitacao.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.embarcacao = embarcacao;
        this.local = local;
        this.cameras = cameras;
        this.internet = internet;
        this.telefonia = telefonia;
        this.itemSimples = itemSimples;
        this.itemUrgente = itemUrgente;
        //testando como imprimi a data
        System.out.println(this.inicio+"\n "+ this.fim);
    }

    //Impressão do objeto
    public String toString() {
                
        return this.inicio + "\n"
                + this.fim + "\n"
                + this.embarcacao + "\n"
                + this.local + "\n"
                + printArray(this.cameras) + "\n"
                + printArray(this.internet) + "\n"
                + printArray(this.telefonia) + "\n"
                + this.itemSimples + "\n"
                + this.itemUrgente;
    }

    //Auxilia na impressão do objeto no metodo toString()
    public String printArray(String[] array){
        String valor = "";
        for(String element : array){
            valor = valor +" , " + element;
        }
        return valor;
    }
    
}
