package dao;

import java.util.List;
import model.Abilita;

public interface DaoInterfaceAbilita {

    public Abilita save(Abilita abilita);

    public List<Abilita> findAll();

    public Abilita findByNome(String nomeAbilita);

    public Abilita findOrCreate(String nomeAbilita);

    public Abilita update(Abilita abilita);

    public boolean delete(String nomeAbilita);
}