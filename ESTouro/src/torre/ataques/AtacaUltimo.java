package torre.ataques;

import java.awt.Point;
import java.util.List;

import bloon.Bloon;

public class AtacaUltimo implements ModoAtaque {


    @Override
    public Point escolherPosicao(List<Bloon> bloons, Point centro) {
        if (bloons == null || bloons.isEmpty())
            return null;
        Bloon bu = bloons.stream().min((b1, b2) -> b1.getPosicaoNoCaminho() - b2.getPosicaoNoCaminho()).get();
        return bu.getComponente().getPosicaoCentro();
    }
    
}
