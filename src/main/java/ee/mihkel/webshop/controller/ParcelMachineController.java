package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.controller.model.OmnivaParcelMachine;
import ee.mihkel.webshop.controller.model.ParcelMachines;
import ee.mihkel.webshop.controller.model.SmartPostParcelMachine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ParcelMachineController {

    @GetMapping("parcel-machines/{country}")
    // {omniva: OmnivaParcelMachines[], smartpost: SmartPostParcelMachines[]}
    public ResponseEntity<ParcelMachines> getParcelMachines(@PathVariable String country) {
//        country = country.toLowerCase();
        // teeb HTTP päringu omniva.ee/locations.json lehele
        // võtab kõik pakiautomaadid

        RestTemplate restTemplate = new RestTemplate();

//        HttpEntity httpEntity = new HttpEntity(null); <-- Body ja Headerid
        ResponseEntity<OmnivaParcelMachine[]> omnivaResponse = restTemplate
                .exchange("https://www.omniva.ee/locations.json",
                        HttpMethod.GET, null,OmnivaParcelMachine[].class);

        ResponseEntity<SmartPostParcelMachine[]> smartPostResponse = restTemplate
                .exchange("https://www.smartpost.ee/places.json",
                        HttpMethod.GET, null,SmartPostParcelMachine[].class);

        ParcelMachines parcelMachines = new ParcelMachines();

        if (omnivaResponse.getBody() != null) {
            List<OmnivaParcelMachine> omnivaParcelMachines = Arrays.asList(omnivaResponse.getBody());
//            List<OmnivaParcelMachine> filteredParcelMachines = new ArrayList<>();
//            for (OmnivaParcelMachine o: omnivaParcelMachines) {
//                if (o.getA0_NAME().equals(country)) {
//                    filteredParcelMachines.add(o);
//                }
//            }
            omnivaParcelMachines = omnivaParcelMachines.stream()
                    .filter(e -> e.getA0_NAME().equals(country))
                    .collect(Collectors.toList());

            parcelMachines.setOmniva(omnivaParcelMachines);

        }

        if (smartPostResponse.getBody() != null && country.equals("EE")) {
            parcelMachines.setSmartpost(Arrays.asList(smartPostResponse.getBody()));
        } else {
            parcelMachines.setSmartpost(new ArrayList<>());
        }
        // lõpuks teeb return
        return new ResponseEntity<>(parcelMachines, HttpStatus.OK);
    }

//    @GetMapping("parcel-machines2")
//    public SmartPostParcelMachine[] getParcelMachines2() {
//        // teeb HTTP päringu omniva.ee/locations.json lehele
//        // võtab kõik pakiautomaadid
//
//        RestTemplate restTemplate = new RestTemplate();
//
////        HttpEntity httpEntity = new HttpEntity(null); <-- Body ja Headerid
//        ResponseEntity<SmartPostParcelMachine[]> response = restTemplate
//                .exchange("https://www.smartpost.ee/places.json",
//                        HttpMethod.GET, null,SmartPostParcelMachine[].class);
//
//        // lõpuks teeb return
//        return response.getBody();
//    }
}
