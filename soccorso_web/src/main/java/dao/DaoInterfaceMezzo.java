/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Mezzo;


public interface DaoInterfaceMezzo {
    public Mezzo save(Mezzo mezzo);
    public List<Mezzo> findAll();
    public Mezzo update(Mezzo mezzo);
    public boolean delete(String targaMezzo);
    public Mezzo findByTarga(String targa);
    public List<Mezzo> findDisponibili();
    public boolean isDisponibile(String targa);
}
