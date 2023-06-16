package pl.damian.bodzioch.dao.Impl;

import org.springframework.stereotype.Repository;
import pl.damian.bodzioch.dao.database.DataInLists;
import pl.damian.bodzioch.dao.HeroDAO;

import java.util.List;
import java.util.Optional;

@Repository
public class HeroDAOImpl implements HeroDAO {

    @Override
    public List<String> getFiveHeroByPattern(String pattern) {
        List<String> options = DataInLists.HERO_NAMES.stream()
                .filter(name -> name.startsWith(pattern))
                .map(String::toLowerCase)
                .toList();

        if (options.size() > 5) {
            return options.subList(0, 4);
        }
        return options;
    }

    @Override
    public Optional<String> getHeroByName(String name) {
        return DataInLists.HERO_NAMES.stream()
                .filter(heroName -> heroName.equals(name))
                .findAny();
    }
}
