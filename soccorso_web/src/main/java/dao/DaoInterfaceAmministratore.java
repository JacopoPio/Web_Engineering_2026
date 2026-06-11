package dao;

import java.util.List;
import model.Amministratore;

public interface DaoInterfaceAmministratore {

    public Amministratore save(Amministratore admin);

    public List<Amministratore> findAll();

    public Amministratore findByEmail(String email);

    public Amministratore update(Amministratore admin);

    public boolean delete(String emailAdmin);
    
     public boolean verificaLogin(String username, String passwordInChiaro);
}
