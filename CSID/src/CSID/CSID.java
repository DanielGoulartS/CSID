package CSID;

import Cadastro.Administrador;
import Cadastro.janelaEntrar;

/**
 *
 * @author Daniel
 */
//Classe principal
public class CSID{

    public static void main(String[] args) {
        
        Administrador romario = new Administrador(0, "Romario", "Surino", "RSURI", "ZXC".toCharArray(), "Adm");
        
        romario.exibir(new janelaEntrar(romario));
        
    }

}
