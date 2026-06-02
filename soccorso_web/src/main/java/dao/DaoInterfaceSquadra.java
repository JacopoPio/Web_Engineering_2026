/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Squadra;


public interface DaoInterfaceSquadra {
    public Squadra save(Squadra squadra);
    public List<Squadra> findAll();
    public Squadra update(Squadra squadra);
    public boolean delete(int idSquadra);
}
