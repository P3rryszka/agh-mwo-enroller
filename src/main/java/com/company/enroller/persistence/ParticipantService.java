package com.company.enroller.persistence;

import java.util.Collection;
import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
//		String hql = "FROM Participant ORDER BY :sortBy";
//		return query.list();
		return null;
	}

	public Participant findById(String login)
	{
		String hql = "FROM Participant WHERE login = :login";
		Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);
		query.setParameter("login", login);
		return query.uniqueResult();
	}

	public void addParticipant(Participant participant)
	{
		Transaction transaction = connector.getSession().getTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void deleteParticipant(Participant participant)
	{
		Transaction transaction = connector.getSession().getTransaction();
		connector.getSession().remove(participant);
		transaction.commit();
	}

	public void updateParticipant(Participant participant)
	{
		Transaction transaction = connector.getSession().getTransaction();
		connector.getSession().update(participant);
		transaction.commit();
	}
}
