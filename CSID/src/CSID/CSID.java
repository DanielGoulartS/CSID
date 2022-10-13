package CSID;

import Cadastro.Administrador;
import Cadastro.JanelaCadastrarEmbarcacoes;
import Cadastro.JanelaCadastrarPortos;
import Cadastro.JanelaCadastrarUsuarios;

/**
 *
 * @author Daniel
 */
//Classe principal
public class CSID{

    public static void main(String[] args) {
        
        Administrador romario = new Administrador(0, "ROmario", "Surino", "RSURI", "ZXC".toCharArray(), "Adm");
        
        romario.exibir(new JanelaCadastrarUsuarios(romario));
        
    }

}
