/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Amministratore;


public interface DaoInterfaceAmministratore {
    public Amministratore save(Amministratore admin);
    public List<Amministratore> findAll();
    public Amministratore update(Amministratore admin);
    public boolean delete(String emailAdmin);
}
