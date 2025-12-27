package torre;

import java.awt.Image;
import java.awt.image.BufferedImage;
import prof.jogos2D.util.ImageLoader;

public class FactoryMorteiro implements FabricaTorre {

    @Override
    public Torre criaTorre(ImageLoader loader) {
        Image img = loader.getImage("data/torres/morteiro/imagem.gif");
        return new TorreMorteiro((BufferedImage) img);
    }
    
}
