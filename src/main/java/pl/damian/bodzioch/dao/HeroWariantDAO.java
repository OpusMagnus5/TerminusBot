package pl.damian.bodzioch.dao;

import java.util.Optional;

public interface HeroWariantDAO {
    Optional<String> getHeroWariantByName(String name);
}
