/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.*;
import model.Operatore;


public interface DaoInterfaceOperatore {
    public Operatore save(Operatore mezzo);
    public List<Operatore> findAll();
    public Operatore update(Operatore mezzo);
    public boolean delete(Operatore mezzo);
}
