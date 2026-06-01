/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

/**
 *
 * @author alesp
 */
import java.util.List;
import model.Materiale;


public interface DaoInterfaceMateriale {
    public Materiale save(Materiale materiale);
    public List<Materiale> findAll();
    public Materiale update(Materiale materiale);
    public boolean delete(int idMateriale);
}

