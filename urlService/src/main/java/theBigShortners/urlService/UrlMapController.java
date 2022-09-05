package theBigShortners.urlService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.*;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class UrlMapController {

    private final UrlMapRepository repository;
    private final HashMap<String, UrlMap> fakeDB = new HashMap<>();

    UrlMapController(UrlMapRepository repo) {
        this.repository = repo;
    }
    // @PostMapping("/url/short", headers = {"content-type=application/x-www-form-urlencoded"})
    /**
     * Generate a key for longUrl, create UrlMap, store them in HashMap
     * @param longUrl url to be shorten
     * @return the UrlMap
     */
    @PostMapping("/url/short")
    UrlMap shortenUrl(@RequestBody String longUrl) {
        String key = Util.keyGen(11);
        UrlMap map = new UrlMap(longUrl, key);
        fakeDB.put(key, map);
        return map;
        //return repository.save(map);
    }

    /**
     * given the key, find the matching long url
     * @param key 11-bit key, part of the shortened url link
     * @return the UrlMap that contains everything
    @GetMapping("/url/resolve")
    String resolveUrl(@RequestBody String key) {
        if (fakeDB.containsKey(key)) {
            Util.browse(fakeDB.get(key).getLongUrl());
            return "Webpage is opened in web browser.";
        }
        else {
            throw new UrlNotFoundException(0L);
        }
    }
    */

    @GetMapping(value = "/url/resolve/{key}")
    ResponseEntity<Void> resolveUrl(@PathVariable String key) {
        if (fakeDB.containsKey(key)) {
            String longUrl = fakeDB.get(key).getLongUrl();
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
        }
        else {
            throw new UrlNotFoundException(0L);
        }
    }

    @GetMapping("/url")
    List<UrlMap> all() {
        return new ArrayList<>(fakeDB.values());
        //return repository.findAll();
    }

    @PostMapping("/url")
    UrlMap newUrlMap(@RequestBody UrlMap newMap) {
        fakeDB.put(newMap.getKey(), newMap);
        return newMap;
        //return repository.save(newMap);
    }

    @GetMapping("/url/{id}")
    UrlMap one(@PathVariable Long id) {
        return null;
        //return repository.findById(id).orElseThrow(() -> new UrlNotFoundException(id));
    }

    @PutMapping("/url/{id}")
    UrlMap replaceUrlMap(@RequestBody UrlMap newMap, @PathVariable Long id) {
        return null;
        /*
        return repository.findById(id).map(urlMap -> {
            urlMap.setKey(newMap.getKey());
            return repository.save(urlMap);
        }).orElseGet(() -> {
            newMap.setId(id);
            return repository.save(newMap);
        });
        */
    }

    @DeleteMapping("/url/{id}")
    void deleteUrlMap(@PathVariable Long id) {
       // repository.deleteById(id);
    }

}
