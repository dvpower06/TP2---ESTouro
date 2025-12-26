package torre.ataques;

import java.awt.Point;
import java.util.List;

import bloon.Bloon;


public interface ModoAtaque {

    

    Point escolherPosicao(List<Bloon> bloons, Point centro);
    // Era preciso a lista dos bloons e a torre para nao ter q ser preciso saber a pos dela


}
