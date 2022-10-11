package CSID;

import Cadastro.Administrador;
import Cadastro.JanelaCadastrarPortos;

/**
 *
 * @author Daniel
 */
//Classe principal
public class CSID{

    public static void main(String[] args) {
        
        Administrador romario = new Administrador(0, "ROmario", "Surino", "RSURI", "ZXC", "Adm");
        
        romario.exibir(new JanelaCadastrarPortos(romario));
        
    }

}
