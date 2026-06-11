package dao;

import java.util.List;
import model.Patente;

public interface DaoInterfacePatente {

    public Patente save(Patente patente);

    public List<Patente> findAll();

    public Patente findByTipo(String tipoPatente);

    public Patente findOrCreate(String tipoPatente);

    public Patente update(Patente patente);

    public boolean delete(String tipoPatente);
}