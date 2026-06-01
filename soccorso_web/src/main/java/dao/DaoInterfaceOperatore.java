/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.*;
import model.Operatore;


public interface DaoInterfaceOperatore {
    public Operatore save(Operatore op);
    public List<Operatore> findAll();
    public Operatore update(Operatore op);
    public boolean delete(Operatore op);
}
