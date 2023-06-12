package pl.damian.bodzioch.dao;

import java.util.Optional;

public interface SiatkaDAO {
    Optional<String> getSiatkaByHeroName(String name);
}
