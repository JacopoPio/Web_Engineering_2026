/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Missione;


public interface DaoInterfaceMissione {
    public Missione save(Missione mezzo);
    public List<Missione> findAll();
    public Missione update(Missione mezzo);
    public boolean delete(Missione mezzo);
}
