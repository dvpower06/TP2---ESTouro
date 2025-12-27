package torre;

import prof.jogos2D.util.ImageLoader;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class FactoryNinja implements FabricaTorre {

    @Override
    public Torre criaTorre(ImageLoader loader) {
        Image img = loader.getImage("data/torres/ninja/imagem.gif");
        return new TorreNinja((BufferedImage) img);
       
    }
    
}
