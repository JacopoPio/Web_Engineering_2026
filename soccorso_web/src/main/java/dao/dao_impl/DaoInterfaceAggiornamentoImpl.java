package dao.dao_impl;

import dao.DaoInterfaceAggiornamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Aggiornamento;


public class DaoInterfaceAggiornamentoImpl implements DaoInterfaceAggiornamento {
    private final EntityManager entityManager;
    
    public DaoInterfaceAggiornamentoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Aggiornamento save(Aggiornamento aggiornamento) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Aggiornamento salvato = this.entityManager.merge(aggiornamento);
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
    public List<Aggiornamento> findAll() {
        String jpql = "SELECT a FROM Aggiornamento a";
        TypedQuery<Aggiornamento> query = this.entityManager.createQuery(jpql, Aggiornamento.class);
        return query.getResultList();
    }

    @Override
    public Aggiornamento update(Aggiornamento aggiornamento) {
        return this.save(aggiornamento); 
    }

    @Override
    public boolean delete(int idAggiornamento) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Aggiornamento aggiornamento = entityManager.find(Aggiornamento.class, idAggiornamento);
            if (aggiornamento != null) {
                entityManager.remove(aggiornamento);
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
