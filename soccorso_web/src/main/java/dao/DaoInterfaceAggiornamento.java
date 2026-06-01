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
import model.Aggiornamento;


public interface DaoInterfaceAggiornamento {
    public Aggiornamento save(Aggiornamento aggiornamento);
    public List<Aggiornamento> findAll();
    public Aggiornamento update(Aggiornamento aggiornamento);
    public boolean delete(Aggiornamento aggiornamento);
}
