/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dao_impl;

import dao.DaoInterfacePatente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Patente;


public class DaoInterfaceImplPatente implements DaoInterfacePatente {
    private final EntityManager entityManager;
    public DaoInterfaceImplPatente(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Patente save(Patente patente) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Patente salvata = this.entityManager.merge(patente);
            tx.commit();
            return salvata;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Patente> findAll() {
        String jpql = "SELECT p FROM Patente p";
        TypedQuery<Patente> query = this.entityManager.createQuery(jpql, Patente.class);
        return query.getResultList();
    }

    @Override
    public Patente update(Patente abilita) {
        return this.save(abilita); 
    }

    @Override
    public boolean delete(String tipoPatente) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Patente patente = entityManager.find(Patente.class, tipoPatente);
            if (patente != null) {
                entityManager.remove(patente);
                tx.commit();
                return true;
            }
            tx.commit();
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    
}
