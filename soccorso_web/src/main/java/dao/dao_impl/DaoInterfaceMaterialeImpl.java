/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dao_impl;

import dao.DaoInterfaceMateriale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Materiale;


public class DaoInterfaceMaterialeImpl implements DaoInterfaceMateriale {
    private final EntityManager entityManager;
    public DaoInterfaceMaterialeImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Materiale save(Materiale materiale) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Materiale salvata = this.entityManager.merge(materiale);
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
    public List<Materiale> findAll() {
        String jpql = "SELECT m FROM Materiale m";
        TypedQuery<Materiale> query = this.entityManager.createQuery(jpql, Materiale.class);
        return query.getResultList();
    }

    @Override
    public Materiale update(Materiale abilita) {
        return this.save(abilita); 
    }

    @Override
    public boolean delete(int idMateriale) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Materiale materiale = entityManager.find(Materiale.class, idMateriale);
            if (materiale != null) {
                entityManager.remove(materiale);
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
