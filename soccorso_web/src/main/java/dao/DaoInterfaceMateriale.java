package dao;

import java.util.List;
import model.Materiale;

public interface DaoInterfaceMateriale {

    public Materiale save(Materiale materiale);

    public List<Materiale> findAll();

    public Materiale findByTipo(String tipo);

    public Materiale update(Materiale materiale);

    public boolean delete(String tipoMateriale);
    public List<Materiale> findDisponibili();
    boolean isDisponibile(Long id);
    public Materiale findById(Long id);
    
}