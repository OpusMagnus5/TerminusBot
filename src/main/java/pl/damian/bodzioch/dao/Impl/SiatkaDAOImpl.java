package pl.damian.bodzioch.dao.Impl;

import org.springframework.stereotype.Repository;
import pl.damian.bodzioch.dao.DataInLists;
import pl.damian.bodzioch.dao.SiatkaDAO;

import java.util.Optional;

@Repository
public class SiatkaDAOImpl implements SiatkaDAO {

    @Override
    public Optional<String> getSiatkaByHeroName(String name) {
        return DataInLists.HERO_SIATKA_LIST.stream()
                .filter(heroName -> heroName.equals(name))
                .findAny();
    }
}
