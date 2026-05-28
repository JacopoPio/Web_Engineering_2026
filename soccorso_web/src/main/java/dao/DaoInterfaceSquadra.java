/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Squadra;


public interface DaoInterfaceSquadra {
    public Squadra save(Squadra mezzo);
    public List<Squadra> findAll();
    public Squadra update(Squadra mezzo);
    public boolean delete(Squadra mezzo);
}
