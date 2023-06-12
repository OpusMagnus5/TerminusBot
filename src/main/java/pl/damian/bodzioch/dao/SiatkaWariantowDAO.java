package pl.damian.bodzioch.dao;

import java.util.Optional;

public interface SiatkaWariantowDAO {

    Optional<String> getSiatkaWariantowByHeroName(String name);
}
