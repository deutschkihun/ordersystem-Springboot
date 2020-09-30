package jpabook.jpashop.domain.controller;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class itemController {

    private final ItemService itemService;

    @RequestMapping("/items/pr")
    public String ProductRegister(){
        log.info("product register controller");
        return "ProductRegister/ProductRegisterForm";
    }


    //=== BOOK ===///
    @GetMapping("/items/pr/BookNew")
    public String createBookForm(Model model){
        model.addAttribute("form",new BookForm());  // attributeName = view variable
        return "items/createBookForm";
    }

    @PostMapping("items/pr/BookNew")
    public String createBook(@Valid @ModelAttribute("form") BookForm form, BindingResult bindingResult){

        // 참고 : 스프링은 bookForm 이라는 객체를 자동으로 model 에 넣어 줍니다.
        // 이떄 넣어줄때에는 model.addAttribute("bookForm", new BookForm)
        // model 은 객체의 이름을 앞글자면 소문자로 처리해서 (즉 BookForm -> bookForm) 넘겨준다
        // 따라서 본인이 매개변수에서 지정한 객체의 이름이 model 의 이름과 같지 않게 설정해주고 싶다면 반드시 @ModelAttribute를 써야 한다.
        // 또한 바로위의 GetMapping 또한 bookForm 으로 정확히 맞춰주어야 작동한다.
        // 마지막으로 items/createItemForm 에서 오브젝트를 받는 부분도 bookForm 으로 정확히 맞춰주어야 한다 (th:object="${bookForm}" method="post">)
        // 만약 나는 bookForm 이 아닌 다른 이름으로 하고 싶다면  oneNote 의 설명을 참고 !!

        if(bindingResult.hasErrors()){
           return "items/createBookForm";
        }

        Book book = new Book();
        book.createBook(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(),form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/";
    }


    //=== Moive ===///
    @GetMapping("/items/pr/MovieNew")
    public String createMovieForm(Model model){
        model.addAttribute("form",new MovieForm());
        return "items/createMovieForm";
    }

    @PostMapping("items/pr/MovieNew")
    public String createMovie(@Valid @ModelAttribute("form") MovieForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "items/createMovieForm";
        }
        Movie movie = new Movie();
        movie.createMovie(form.getName(), form.getPrice(), form.getStockQuantity(), form.getDirector(),form.getGenre());
        itemService.saveItem(movie);
        return "redirect:/";
    }


    //=== Album ===///
    @GetMapping("/items/pr/AlbumNew")
    public String createAlbumForm(Model model){
        model.addAttribute("form",new AlbumForm());
        return "items/createAlbumForm";
    }

    @PostMapping("items/pr/AlbumNew")
    public String createAlbum(@Valid @ModelAttribute("form") AlbumForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "items/createAlbumForm";
        }
        Album album = new Album();
        album.createAlbum(form.getName(), form.getPrice(), form.getStockQuantity(), form.getArtist(),form.getGenre());
        itemService.saveItem(album);
        return "redirect:/";
    }

    //==Item Modify==//
    @GetMapping("/items/pm")
    public String ProductModify(){
        log.info("product register controller");
        return "ProductModify/ProductModifyForm";
    }

    @GetMapping("/items/pm/BookModify")
    public String BookList(Model model){
        model.addAttribute("items",itemService.findBookItems());
        return "items/itemList";
    }

    @GetMapping(value = "/items/pm/BookModify/{itemId}/edit")
    public String updateBookForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping(value = "/items/pm/BookModify/{itemId}/edit")
    public String updateBook(@Valid @ModelAttribute("form") BookForm form, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "items/updateItemForm";
        }
        //itemService.updateItem(form.getId(), form.getName(), form.getPrice(),form.getStockQuantity());

        //방법 1 : merge
        // itemService.saveItem 을 사용하게 되면 역추적에 의해 em.merge 를 사용한다.
        // merge 는 매겨변수가 필요로 하는 데이터를 단 1개라로 뺴먹으면 그것을  null 처리한다.
        // 왜냐  merge 는 변경감지와 달리 모든 데이터를 갈아 끼우기 때문이다.
        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);

        // 방법 2 : 변경감지
        // 변경감지를 사용하면 변경이 된 것만 업데이트를 시켜주고 변하지 않은것은 그대로 유지해준다.
        // 다만 이경우에 author 와 isbn 은 변경감지로 처리할수 없다.
        // 왜냐하면 updateItem method 는 itemRepository 에서 찾은 item 을 이용하며 item 은 book 의 부모클래스이다.
        // 부모클래스는 자식클래스의 메소드,필드를 사용할수 없다.
        //itemService.updateItem(form.getId(), form.getName(), form.getPrice(),form.getStockQuantity());
        return "redirect:/items/pm/BookModify";
    }


    //==Movie Modify==//
    @GetMapping("/items/pm/MovieModify")
    public String MovieList(Model model){
        model.addAttribute("items2",itemService.findMovieItems());
        return "items/itemList2";
    }

    @GetMapping(value = "/items/pm/MovieModify/{itemId}/edit")
    public String updateMovieForm(@PathVariable("itemId") Long itemId, Model model) {
        Movie item = (Movie) itemService.findOne(itemId);

        MovieForm form = new MovieForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setDirector(item.getDirector());
        form.setGenre(item.getGenre());
        model.addAttribute("form", form);
        return "items/updateItemForm2";
    }

    @PostMapping(value = "/items/pm/MovieModify/{itemId}/edit")
    public String updateMovie(@Valid @ModelAttribute("form") MovieForm form, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "items/updateItemForm2";
        }
        Movie movie = new Movie();
        movie.setId(form.getId());
        movie.setName(form.getName());
        movie.setPrice(form.getPrice());
        movie.setStockQuantity(form.getStockQuantity());
        movie.setDirector(form.getDirector());
        movie.setGenre(form.getGenre());
        itemService.saveItem(movie);
        return "redirect:/items/pm/MovieModify";
    }


    //==Album Modify==//
    @GetMapping("/items/pm/AlbumModify")
    public String AlbumList(Model model){
        model.addAttribute("items3",itemService.findAlbumItems());
        return "items/itemList3";
    }

    @GetMapping(value = "/items/pm/AlbumModify/{itemId}/edit")
    public String updateAlbumForm(@PathVariable("itemId") Long itemId, Model model) {
        Album item = (Album) itemService.findOne(itemId);

        AlbumForm form = new AlbumForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setArtist(item.getArtist());
        form.setGenre(item.getGenre());
        model.addAttribute("form", form);
        return "items/updateItemForm3";
    }

    @PostMapping(value = "/items/pm/AlbumModify/{itemId}/edit")
    public String updateAlbum(@Valid @ModelAttribute("form") AlbumForm form, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "items/updateItemForm3";
        }
        Album album = new Album();
        album.setId(form.getId());
        album.setName(form.getName());
        album.setPrice(form.getPrice());
        album.setStockQuantity(form.getStockQuantity());
        album.setArtist(form.getArtist());
        album.setGenre(form.getGenre());
        itemService.saveItem(album);
        return "redirect:/items/pm/AlbumModify";
    }



























}
