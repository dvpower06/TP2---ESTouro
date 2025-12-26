package torre.ataques;

import java.awt.Point;
import java.util.List;


import bloon.Bloon;

public class AtacaPrimeiro implements ModoAtaque {

    
    @Override          
    public Point escolherPosicao(List<Bloon> bloons, Point centro) {
        if (bloons == null || bloons.isEmpty())
            return null;
        Bloon bp = bloons.stream().max((b1, b2) -> b1.getPosicaoNoCaminho() - b2.getPosicaoNoCaminho()).get();
        return bp.getComponente().getPosicaoCentro();
       
    }
    
}
