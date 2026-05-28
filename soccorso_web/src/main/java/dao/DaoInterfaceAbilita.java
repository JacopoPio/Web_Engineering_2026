/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Abilita;


public interface DaoInterfaceAbilita {
    public Abilita save(Abilita mezzo);
    public List<Abilita> findAll();
    public Abilita update(Abilita mezzo);
    public boolean delete(Abilita mezzo);
}
