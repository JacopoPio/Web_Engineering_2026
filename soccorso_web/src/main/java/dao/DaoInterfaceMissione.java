/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Missione;
import model.Richiesta;


public interface DaoInterfaceMissione {
    public Missione save(Missione missione);
    public List<Missione> findAll();
    public Missione findById(int idMissione);
    public List<Missione> findByOperatore(String emailOperatore);
    public List<Missione> findByMezzo(String targaMezzo);
    public List<Missione> findByMateriale(Long idMateriale);
    public Missione update(Missione missione);
    public boolean delete(int idMissione);
    public boolean existsByRichiesta(Richiesta richiesta);
}
