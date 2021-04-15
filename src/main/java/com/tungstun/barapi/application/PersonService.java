package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final SpringPersonRepository SPRING_PERSON_REPOSITORY;
    private final BarService BAR_SERVICE;

    public PersonService(SpringPersonRepository SPRING_PERSON_REPOSITORY, BarService BAR_SERVICE) {
        this.SPRING_PERSON_REPOSITORY = SPRING_PERSON_REPOSITORY;
        this.BAR_SERVICE = BAR_SERVICE;
    }

    public Person getPersonOfBar(Long barId, Long personId) throws NotFoundException {
        List<Person> people = getAllPeopleOfBar(barId);
        for(Person person : people){
            if(person.getId().equals(personId)) return person;
        }
        throw new NotFoundException("Bar does not have a person with id: " + personId);
    }

    public List<Person> getAllPeopleOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Person> people = bar.getUsers();
        if (people.isEmpty()) throw new NotFoundException("There are no people available for this bar");
        return people;
    }

    public Person createNewPerson(Long barId, PersonRequest personRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        if (checkIfPersonExists(bar.getUsers(), personRequest.name))
            throw new DuplicateRequestException(String.format("User with name '%s' already exists", personRequest.name));
        PersonBuilder personBuilder = new PersonBuilder()
                .setName(personRequest.name)
                .setPhoneNumber(personRequest.phoneNumber);
        Person person = personBuilder.build();
        return savePersonToBar(bar, person);
    }

    private boolean checkIfPersonExists(List<Person> people, String name) {
        for (Person person : people) {
            if (person.getName().equals(name)) return true;
        }
        return false;
    }

    private Person savePersonToBar(Bar bar, Person person) {
        person = this.SPRING_PERSON_REPOSITORY.save(person);
        bar.addUser(person);
        this.BAR_SERVICE.saveBar(bar);
        return person;
    }

    public Person updatePerson(Long barId, Long personId, PersonRequest personRequest) throws NotFoundException {
        Person person = getPersonOfBar(barId, personId);
        person.setName(personRequest.name);
        person.setPhoneNumber(personRequest.phoneNumber);
        return this.SPRING_PERSON_REPOSITORY.save(person);
    }

    public void  deletePersonFromBar(Long barId, Long personId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Person person = getPersonOfBar(barId, personId);
        bar.removeUser(person);
        this.BAR_SERVICE.saveBar(bar);
    }
}
