package Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Solicitacao {

    //Variaveis
    private Date inicio, fim;
    private String embarcacao, local, itemSimples, itemUrgente;
    private String cameras, internet, telefonia;

    //Consrução do Objeto
    public Solicitacao(
            String inicio, String fim,
            String embarcacao, String local,
            String cameras, String internet, String telefonia,
            String itemSimples, String itemUrgente) {

        this.inicio = stringToDate(inicio);
        this.fim = stringToDate(fim);
        this.embarcacao = embarcacao;
        this.local = local;
        this.cameras = cameras;
        this.internet = internet;
        this.telefonia = telefonia;
        this.itemSimples = itemSimples;
        this.itemUrgente = itemUrgente;
        //testando como imprime a data
        System.out.println(this.inicio + "\n " + this.fim);
        System.out.println();
        for(int i = 0; i < 15; i++)
            System.out.print("-");
    }

    //Impressão do objeto
    public String toString() {
        return dateToString(this.inicio) + "\n"
                + dateToString(this.fim) + "\n"
                + this.embarcacao + "\n"
                + this.local + "\n"
                + this.cameras + "\n"
                + this.internet + "\n"
                + this.telefonia + "\n"
                + this.itemSimples + "\n"
                + this.itemUrgente;
    }

    //Converte a String recebida em Date
    public Date stringToDate(String data) {
        Date data1 = new Date();
        try {
            SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
            data1 = sdt.parse(data);
            return data1;
        } catch (ParseException ex) {
            System.err.println("Falha na conversão: CSID.Classes.Solicitacao.formatarData()");
        } finally {
            return data1;
        }
    }

    //Converte a Date recebida em String
    public String dateToString(Date data) {
        String data1 = "";
        SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
        data1 = sdt.format(data);
        return data1;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public String getEmbarcacao() {
        return embarcacao;
    }

    public void setEmbarcacao(String embarcacao) {
        this.embarcacao = embarcacao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getItemSimples() {
        return itemSimples;
    }

    public void setItemSimples(String itemSimples) {
        this.itemSimples = itemSimples;
    }

    public String getItemUrgente() {
        return itemUrgente;
    }

    public void setItemUrgente(String itemUrgente) {
        this.itemUrgente = itemUrgente;
    }

    public String getCameras() {
        return cameras;
    }

    public void setCameras(String cameras) {
        this.cameras = cameras;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getTelefonia() {
        return telefonia;
    }

    public void setTelefonia(String telefonia) {
        this.telefonia = telefonia;
    }

}
