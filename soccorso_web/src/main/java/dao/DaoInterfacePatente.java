/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;


import java.util.List;
import model.Patente;


public interface DaoInterfacePatente  {
    public Patente save(Patente patente);
    public List<Patente> findAll();
    public Patente update(Patente patente);
    public boolean delete(String tipoPatente);
}
