package apresentacao;

import org.neo4j.driver.*;

import persistencia.PerfilDAO;

import static org.neo4j.driver.Values.parameters;


/*
-- 1ª vez
sudo docker run \
--publish=7474:7474 --publish=7687:7687 \
--volume=$HOME/neo4j/data:/data \
--env=NEO4J_AUTH=neo4j/password \
--name neo4j neo4j

-- demais vezes
sudo docker start neo4j
sudo docker exec -it neo4j bash
 */

public class Main {
    public static void main(String[] args) {
        // new PerfilDAO().deletarTudo();
        new PerfilDAO().adicionarPerfil("Gabriel");
        new PerfilDAO().seguir(41, 44);
        new PerfilDAO().recomendacao(40);
        // new PerfilDAO().seguir(9, 7);
        // new PerfilDAO().adicionarPublicacao(7, "AGORA VAI MARYENE");

        // new PerfilDAO().desfazer(7, 9);
        // new PerfilDAO().desfazer(9, 7);
    }
}