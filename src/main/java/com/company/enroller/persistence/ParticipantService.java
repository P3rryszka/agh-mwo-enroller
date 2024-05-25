package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component
public class ParticipantService {
	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
		String hql = "FROM Participant WHERE login LIKE :login";
		if(sortBy.equals("login")) {
			hql += " ORDER BY " + sortBy;
			if(sortBy.equals("ASC") || sortBy.equals("DESC")) {
				hql += " " + sortOrder;
			}
		}
		Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);
		query.setParameter("login", "%" + key + "%");
		return query.list();
	}

	public Participant findById(String login) {
		String hql = "FROM Participant WHERE login = :login";
		Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);
		query.setParameter("login", login);
		return query.uniqueResult();
	}


	public Participant addParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
		return participant;
	}

	public void deleteParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

	public void updateParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(participant);
		transaction.commit();
	}
}
