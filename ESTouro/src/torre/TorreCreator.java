package torre;

import java.util.HashMap;
import java.util.Map;

import prof.jogos2D.util.ImageLoader;

/**
 * Classe que trata da criação das várias torres. Esta classe existe que lidar
 * com a criação de todas as torres e assim impedir que as outras classes tenham
 * de ser alteradas quando se criam novas torres.
 */
public class TorreCreator {
	private ImageLoader loader = ImageLoader.getLoader();

	//TODO FEITO suportar também a sniper

    private final Map<String, FabricaTorre> catalogo = new HashMap<>();


    public void registarTorre(String nome, FabricaTorre fabrica) {
        catalogo.put(nome, fabrica);
    }

    public Torre criarTorrePorNome(String nome) {
        FabricaTorre f = catalogo.get(nome);
        return (f != null) ? f.criaTorre(loader) : null;
    }


}
