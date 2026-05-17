package com.example.ecmini.controller;

import com.example.ecmini.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.ecmini.entity.Product;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;


//これは画面(HTML)を返すクラスですよ、という印
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            HttpServletRequest request
    ) {

        // ▼ ページング＋検索
        org.springframework.data.domain.Page<Product> productPage =
                productService.searchWithPaging(name, category, page);

        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("category", category);

        // ▼ 最近見た商品（Cookie 読み取り）
        String cookieName = "recentProducts";

        String value = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");

// ★ 古い Cookie（1-17-12-7）にも対応
        value = value.replace("-", ",");

        if (!value.isBlank()) {
            List<Long> ids = Arrays.stream(value.split(","))
                    .filter(v -> !v.isBlank())   // ★ 空文字対策
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            List<Product> recentProducts = productService.findByIds(ids);
            model.addAttribute("recentProducts", recentProducts);
        }

        return "products/list";
    }

    //商品名検索
    @GetMapping("/products/search")
    public String searchByName(String keyword, Model model) {
        model.addAttribute("products", productService.searchByName(keyword));
        return "products/list";
    }

    //新規登録Get
    @GetMapping("/products/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/new";
    }

    //詳細GET表示を追加
    //Cokie機能
    @GetMapping("/products/{id}")
    public String detail(
            @PathVariable Long id,
            Model model,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);

        // ▼ レコメンド（同じカテゴリの商品）
        List<Product> related = productService.findRelatedProducts(product.getCategory(), product.getId());
        model.addAttribute("relatedProducts", related);

        // ▼ 最近見た商品を Cookie に保存
        String cookieName = "recentProducts";
        String newId = String.valueOf(id);

        // 既存 Cookie を取得
        String value = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");

        // ★ 古い Cookie（1-17-12-7）にも対応
        value = value.replace(",", "-");

        // ID を追加（重複は削除）
        List<String> list = new ArrayList<>();

        if (!value.isBlank()) {
            list = new ArrayList<>(
                    Arrays.stream(value.split("-"))
                            .filter(v -> !v.isBlank())   // ★ 空文字を除外
                            .collect(Collectors.toList())
            );
        }

        list.remove(newId); // 重複削除
        list.add(0, newId); // 先頭に追加

        // 最大10件に制限
        if (list.size() > 10) {
            list = list.subList(0, 10);
        }

        // Cookie 保存
        Cookie cookie = new Cookie(cookieName, String.join("-", list));
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30日保持
        response.addCookie(cookie);

        return "products/detail";
    }


    //編集画面GETを追加
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "products/edit";
    }

    //商品削除
    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }

    //商品登録(POST)
    //@PostMapping("/admin/products")
    public String create(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {

        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path path = Paths.get("src/main/resources/static/uploads/" + fileName);
            Files.write(path, imageFile.getBytes());
            product.setImage(fileName);
        }

        productService.save(product);
        return "redirect:/admin/products";
    }
    @GetMapping("/shop-guide")
    public String shopGuide() {
        return "guide/shipping";
    }

    @GetMapping("/how-to-buy")
    public String howToBuy() {
        return "info/how-to-buy"; // templates/info/how-to-buy.html を探す
    }

    @GetMapping("/payment")
    public String payment() {
        return "info/payment";
    }

    @GetMapping("/shipping")
    public String shipping() {
        return "guide/shipping";
    }

    @GetMapping("/returns")
    public String returns() {
        return "info/returns";
    }
}