package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private PersonRepository repository;

    @RequestMapping("/")
    public String index(){
        return "redirect:list";
    }

    @RequestMapping("/list")
    public String list(Model model){
        List<Person> data = new LinkedList<>();
        for (Person p: repository.findAll()){
            data.add(p);
        }
        model.addAttribute("people", data);
        return "list";
    }

    @RequestMapping("/input")
    public String input(){
        return "input";
    }

    @RequestMapping("/create")
    public String create(
            @RequestParam(name="firstname", required=true) String firstname,
            @RequestParam(name="lastname", required=true) String lastname) {
        repository.save(new Person(firstname,lastname));
        return "redirect:/list";
    }



    @RequestMapping("/show")
    public String read(
            @RequestParam(name="id", required=true) Long id,
            Model model) {
        Optional<Person> result = repository.findById(id);
        if(!result.isPresent()) {
            return "notfound";
        }
        Person actualPerson = result.get();

        model.addAttribute("firstName", actualPerson.getFirstName());
        model.addAttribute("lastName", actualPerson.getLastName());
        model.addAttribute("id", actualPerson.getId());
        return "show";
    }



    @RequestMapping("/edit")
    public String edit(
            @RequestParam(name="id", required=true) Long id,
            Model model) {
        Optional<Person> result = repository.findById(id);
        //TODO: check if the result is found
        if(!result.isPresent()){
            return "notfound";
        }
        Person actualPerson = result.get();
        model.addAttribute("firstName", actualPerson.getFirstName());
        model.addAttribute("lastName", actualPerson.getLastName());
        model.addAttribute("id", actualPerson.getId());
        return "edit";
    }

    @RequestMapping("/update")
    public String update(
            @RequestParam(name="id", required=true) Long id,
            @RequestParam(name="firstname", required=true) String firstname,
            @RequestParam(name="lastname", required=true) String lastname,
            Model model) {

        Optional<Person> result = repository.findById(id);
        if(!result.isPresent()){
            return "notfound";
        }
        Person actualPerson = result.get();
        actualPerson.setFirstName(firstname);
        actualPerson.setLastName(lastname);
        repository.save(actualPerson);

        return "redirect:/list";
    }


    @RequestMapping("/delete")
    public String delete(
            @RequestParam(name="id", required=true) Long id) {
        Optional<Person>result = repository.findById(id);
        if(!result.isPresent()){
            return "notfound";
        }
        repository.delete(result.get());
        return "redirect:/list";
    }

}