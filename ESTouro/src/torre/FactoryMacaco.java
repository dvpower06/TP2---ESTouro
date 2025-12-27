package torre;

import java.awt.Image;
import java.awt.image.BufferedImage;

import prof.jogos2D.util.ImageLoader;

public class FactoryMacaco implements FabricaTorre {

    @Override
    public Torre criaTorre(ImageLoader loader) {
        Image img = loader.getImage("data/torres/macaco/imagem.gif");
        return new TorreMacaco((BufferedImage) img);
    }
}
