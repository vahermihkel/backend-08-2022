package ee.mihkel.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class WebshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebshopApplication.class, args);
//        strings = ["Audi", "BMW", "Chevrolet", "Dodge", "Ford"];
        List<String> strings = new ArrayList<>(Arrays.asList("Audi", "BMW", "Chevrolet", "Dodge", "Ford"));
//        new Scanner - Java sisseehitatud klassid
//        new Random - Java sisseehitatud
//        new Product
        Scanner scanner = new Scanner(System.in);
        String newString = scanner.nextLine();
//        .push()  .put()
        strings.add(newString);
//        console.log();
        System.out.println(strings);
    }

}
