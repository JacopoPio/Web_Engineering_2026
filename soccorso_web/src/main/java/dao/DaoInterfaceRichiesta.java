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
import model.Richiesta;

public interface DaoInterfaceRichiesta {
    public Richiesta save(Richiesta mezzo);
    public List<Richiesta> findAll();
    public Richiesta update(Richiesta mezzo);
    public boolean delete(Richiesta mezzo);
}

