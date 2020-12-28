package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.Bar;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.SessionFactory;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    private final SpringSessionRepository SPRING_SESSION_REPOSITORY;
    private final BarService BAR_SERVICE;

    public SessionService(SpringSessionRepository springSessionRepository, BarService barService) {
        this.SPRING_SESSION_REPOSITORY = springSessionRepository;
        this.BAR_SERVICE = barService;
    }

    /**
     * Returns a list with all sessions of bar
     * @return list of sessions
     * @throws NotFoundException if no bar with given id is found or
     *     if bar does not have any sessions
     */
    public List<Session> getAllSessionsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Session> sessions = bar.getSessions();
        if (sessions.isEmpty()) throw new NotFoundException("There are no sessions available for this bar");
        return sessions;
    }

    /**
     * Returns session with given id from bar with given id
     * @return session
     * @throws NotFoundException if no bar with given id is found or
     *     if bar does not have any sessions or
     *     if bar does not have a session with given id
     */
    public Session getSessionOfBar(Long barId, Long sessionId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Session> sessions = bar.getSessions();
        if (sessions.isEmpty()) throw new NotFoundException("There are no sessions available for this bar");
        for(Session session : sessions){
            if(session.getId().equals(sessionId)) return session;
        }
        throw new NotFoundException("Bar does not have a session with id: " + sessionId);
    }

    /**
     * Creates a new session and adds it to bar with given id
     * @return created session
     * @throws NotFoundException if no bar with given id is found
     * @throws DuplicateRequestException if bar already has a duplicate of the session
     */
    public Session createNewSession(Long barId ) throws NotFoundException {
        Bar bar = BAR_SERVICE.getBar(barId);
        Session session = new SessionFactory(LocalDateTime.now()).createSession();
        if(!bar.addSession(session)) throw new DuplicateRequestException("Bar already has this session");
        this.BAR_SERVICE.saveBar(bar);
        return session;
    }

    /**
     * Removes session from sessions of bar with given id and deletes it from the datastore
     * @throws NotFoundException if no bar with given id is found 
     */
    public void deleteSession(Long barId, Long sessionId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Session session = getSessionOfBar(barId, sessionId);
        bar.removeSession(session);
        this.BAR_SERVICE.saveBar(bar);
        this.SPRING_SESSION_REPOSITORY.delete(session);
    }

    /**
     * Saves a Session object
     * @param session to be saved
     */
    public void saveSession(Session session){
        this.SPRING_SESSION_REPOSITORY.save(session);
    }
}
