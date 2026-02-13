package ee.piperal.veebipood.service;


import ee.piperal.veebipood.entity.Person;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PersonService {
    private final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private final Pattern pattern = Pattern.compile(regex);

    public boolean isValid(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void validate(Person person){
        if(person.getId() != null){
            throw new RuntimeException("Cannot sign up with id");
        }
        if(person.getEmail() == null){
            throw new RuntimeException("Cannot sign up with id");
        }
        if(person.getPersonalCode() == null){
            throw new RuntimeException("Cannot sign up with id");
        }
        if(!isValid(person.getEmail())){
            throw new RuntimeException("Invalid email");
        }
    }
}
