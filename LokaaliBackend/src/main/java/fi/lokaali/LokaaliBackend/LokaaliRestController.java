package fi.lokaali.LokaaliBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by possumunnki on 1.4.2018.
 */

@RestController
public class LokaaliRestController {


    @Autowired
    ProductPostRepository productPostRepository;

    @CrossOrigin
    @RequestMapping(value="/products", method=RequestMethod.POST)
    public ResponseEntity<Void> addProductPost(@RequestBody ProductPost productPost, UriComponentsBuilder builder) {
        productPostRepository.save(productPost);
        UriComponents uriComponents =
                builder.path("products/{postID}").buildAndExpand(productPost.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value="/products", method=RequestMethod.GET)
    public Iterable<ProductPost> getProductPosts() {
        return productPostRepository.findAll();
    }

    @CrossOrigin
    @RequestMapping(value="/products/{postID}", method=RequestMethod.GET)
    public ProductPost getProductPost(@PathVariable long postId) {
        ProductPost post = productPostRepository.findById(postId).orElse(null);
        return post;
    }

    @CrossOrigin
    @RequestMapping(value = "/products/{postID}", method = RequestMethod.DELETE)
    public void deleteProductPost(@PathVariable long postID){
        productPostRepository.deleteById(postID);
    }
}
