package com.company.enroller.persistence;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

import java.util.Collection;

@Component
public class MeetingService {
	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAllMeetings() {
		String hql = "FROM Meeting";
		Query<Meeting> query = connector.getSession().createQuery(hql, Meeting.class);
		return query.list();
	}

	public Meeting findById(long id) {
		String hql = "FROM Meeting WHERE id = :id";
		Query<Meeting> query = connector.getSession().createQuery(hql, Meeting.class);
		query.setParameter("id", id);
		return query.uniqueResult();
	}

	public Meeting addMeeting(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();;
		return meeting;
	}

	public void deleteMeeting(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}
}
