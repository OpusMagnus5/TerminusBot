package pl.damian.bodzioch.dao;

import java.util.List;
import java.util.Optional;

public interface HeroDAO {
    List<String> getFiveHeroByPattern(String pattern);
    Optional<String> getHeroByName(String name);
}
