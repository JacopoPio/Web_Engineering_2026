/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Abilita;


public interface DaoInterfaceAbilita {
    public Abilita save(Abilita abilita);
    public List<Abilita> findAll();
    public Abilita update(Abilita abilita);
    public boolean delete(String nome);
}
