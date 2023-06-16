package pl.damian.bodzioch.dao.Impl;

import org.springframework.stereotype.Repository;
import pl.damian.bodzioch.dao.database.DataInLists;
import pl.damian.bodzioch.dao.HeroWariantDAO;

import java.util.Optional;

@Repository
public class HeroWariantDAOImpl implements HeroWariantDAO {
    @Override
    public Optional<String> getHeroWariantByName(String name) {
        return DataInLists.HERO_WARIANT_LIST.stream()
                .filter(heroWariant -> heroWariant.equals(name))
                .findAny();
    }
}
