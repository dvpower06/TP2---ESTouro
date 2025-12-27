package torre;

import java.awt.Image;
import java.awt.image.BufferedImage;

import prof.jogos2D.util.ImageLoader;

public class FactoryOcto implements FabricaTorre {

    @Override
    public Torre criaTorre(ImageLoader loader) {
        Image img = loader.getImage("data/torres/octo/imagem.gif");
        return new TorreOctogonal((BufferedImage) img);
    }
}
