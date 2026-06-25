/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.dao_impl;

import dao.DaoInterfaceMissione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Missione;
import model.Operatore;
import model.Richiesta;

/**
 *
 * @author Jacopo Antonio
 */
public class DaoInterfaceMissioneImpl implements DaoInterfaceMissione {
    
    private EntityManager entityManager;
    public DaoInterfaceMissioneImpl(EntityManager em)
    {
        this.entityManager = em;
    }
    @Override
    public Missione save(Missione missione) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Missione salvato = this.entityManager.merge(missione);

            tx.commit();

            return salvato;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public List<Missione> findAll() {
        this.entityManager.clear();

        String jpql = "SELECT m FROM Missione m";

        TypedQuery<Missione> query =
                this.entityManager.createQuery(jpql, Missione.class);

        return query.getResultList();
    }

    

    @Override
    public Missione update(Missione missione) {
        return this.save(missione);
    }

    @Override
    public boolean delete(int idMissione) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Missione missione =
                    this.entityManager.find(Missione.class, idMissione);

            if (missione != null) {
                this.entityManager.remove(missione);
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

    @Override
    public boolean existsByRichiesta(Richiesta richiesta) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(m) FROM Missione m WHERE m.email_segnalante = :email", Long.class)
            .setParameter("email", richiesta.getEmail_segnalante())
            .getSingleResult();
        return count > 0;
    }
    
}
