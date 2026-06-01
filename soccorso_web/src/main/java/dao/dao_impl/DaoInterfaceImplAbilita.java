package dao.dao_impl;

import dao.DaoInterfaceAbilita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Abilita;

public class DaoInterfaceImplAbilita implements DaoInterfaceAbilita {
    
    private final EntityManager entityManager;
    public DaoInterfaceImplAbilita(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Abilita save(Abilita abilita) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Abilita salvata = this.entityManager.merge(abilita);
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
    public List<Abilita> findAll() {
        String jpql = "SELECT a FROM Abilita a";
        TypedQuery<Abilita> query = this.entityManager.createQuery(jpql, Abilita.class);
        return query.getResultList();
    }

    @Override
    public Abilita update(Abilita abilita) {
        return this.save(abilita); 
    }

    @Override
    public boolean delete(String nomeAbilita) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Abilita abilita = entityManager.find(Abilita.class, nomeAbilita);
            if (abilita != null) {
                entityManager.remove(abilita);
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

