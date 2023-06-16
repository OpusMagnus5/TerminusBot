package pl.damian.bodzioch.dao.Impl;

import org.springframework.stereotype.Repository;
import pl.damian.bodzioch.dao.database.DataInLists;
import pl.damian.bodzioch.dao.SiatkaWariantowDAO;

import java.util.Optional;

@Repository
public class SiatkaWariantowDAOImpl implements SiatkaWariantowDAO {

    @Override
    public Optional<String> getSiatkaWariantowByHeroName(String heroName) {
        return DataInLists.SIATKI_WARIANTOW_LIST.stream()
                .filter(siatkaWariantu -> siatkaWariantu.equals(heroName))
                .findAny();
    }
}
