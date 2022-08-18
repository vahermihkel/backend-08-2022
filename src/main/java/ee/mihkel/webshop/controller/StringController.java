package ee.mihkel.webshop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// ee.mihkel.webshop peale parem klõps -> new Java class -> "StringController"
// StringController klassi üles @Controller, koos impordiga

@RestController // annotiation
public class StringController {
    List<String> strings = new ArrayList<>(Arrays.asList("Audi", "BMW", "Chevrolet", "Dodge", "Ford"));
    Random random = new Random();

    @GetMapping("strings")   // localhost:8080/strings
    public List<String> getStrings() {
        System.out.println(random);
        return strings;
    }

    @GetMapping("strings/{newString}")   // localhost:8080/strings/Mercedes
    public List<String> addString(@PathVariable String newString) {
        strings.add(newString);
        return strings;
    }

    @GetMapping("hi")    // brauserisse või postmani: GET      localhost:8080/hi
    public String sayHello() {
        return "Hello";
    }

    @GetMapping("hi/{personName}")    // brauserisse või postmani: GET      localhost:8080/hi/Kalle
    public String sayHelloToName(@PathVariable String personName) {
        return "Hello " + personName;
    }

    @GetMapping("calculate/{nr1}/{nr2}")    // brauserisse või postmani: GET      localhost:8080/calculate/5/10
    public Integer calculate(@PathVariable int nr1, @PathVariable int nr2) {
        return nr1 * nr2;
    }
}
